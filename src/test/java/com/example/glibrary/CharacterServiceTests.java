package com.example.glibrary;

import com.example.glibrary.DTO.CharacterDto;
import com.example.glibrary.DTO.RegionDto;
import com.example.glibrary.DTO.RelicsDto;
import com.example.glibrary.cache.CharacterCache;
import com.example.glibrary.exception.NotFoundException;
import com.example.glibrary.model.Character;
import com.example.glibrary.model.Rarity;
import com.example.glibrary.model.Region;
import com.example.glibrary.model.Relic;
import com.example.glibrary.repository.CharacterRepository;

import com.example.glibrary.service.CharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CharacterServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private CharacterCache characterCache;

    @InjectMocks
    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method
    private CharacterDto sampleDto() {
        CharacterDto dto = new CharacterDto();
        dto.setId(1L);
        dto.setName("Aether");
        dto.setType("Anemo");
        dto.setRole("DPS");
        dto.setWeapon("Sword");
        dto.setRarity(Rarity.Star5);
        return dto;
    }

    private Character sampleCharacter() {
        Character character = new Character();
        character.setId(1L);
        character.setName("Aether");
        character.setType("Anemo");
        character.setRole("DPS");
        character.setWeapon("Sword");
        character.setRarity(Rarity.Star5);
        return character;
    }

    @Test
    void testCreateCharacter() {
        CharacterDto dto = sampleDto();
        Character entity = sampleCharacter();

        when(characterRepository.save(any())).thenReturn(entity);

        Character result = characterService.createCharacter(dto);

        assertEquals("Aether", result.getName());
        verify(characterRepository).save(any());
    }

    @Test
    void testCreateBulkCharacters() {
        List<CharacterDto> dtoList = List.of(sampleDto());
        when(characterRepository.saveAll(anyList())).thenReturn(List.of(sampleCharacter()));

        List<CharacterDto> result = characterService.createBulkCharacters(dtoList);

        assertEquals(1, result.size());
        assertEquals("Aether", result.get(0).getName());
    }

    @Test
    void testReadCharactersThrowsNotFound() {
        when(characterRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> characterService.readCharacters());
    }

    @Test
    void testReadCharactersSuccess() {
        when(characterRepository.findAll()).thenReturn(List.of(sampleCharacter()));

        List<CharacterDto> result = characterService.readCharacters();

        assertFalse(result.isEmpty());
        assertEquals("Aether", result.get(0).getName());
    }

    @Test
    void testReadCharacterByNameFromCache() {
        CharacterDto dto = sampleDto();
        when(characterCache.get("Aether")).thenReturn(Optional.of(dto));

        CharacterDto result = characterService.readCharacterByName("Aether");

        assertEquals("Aether", result.getName());
    }

    @Test
    void testReadCharacterByNameFromRepo() {
        Character character = sampleCharacter();
        when(characterCache.get("Aether")).thenReturn(Optional.empty());
        when(characterRepository.findByName("Aether")).thenReturn(Optional.of(character));

        CharacterDto result = characterService.readCharacterByName("Aether");

        assertEquals("Aether", result.getName());
        verify(characterCache).put(eq("Aether"), any());
    }

    @Test
    void testReadCharacterByNameThrowsNotFound() {
        when(characterCache.get("Unknown")).thenReturn(Optional.empty());
        when(characterRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> characterService.readCharacterByName("Unknown"));
    }

    @Test
    void testReadCharacterByTypeAndRoleSuccess() {
        when(characterRepository.findByTypeAndRole("Anemo", "DPS")).thenReturn(List.of(sampleCharacter()));

        List<CharacterDto> result = characterService.readCharacterByTypeAndRole("Anemo", "DPS");

        assertEquals("Aether", result.get(0).getName());
    }

    @Test
    void testReadCharacterByTypeAndRoleThrows() {
        when(characterRepository.findByTypeAndRole("Geo", "Support")).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> characterService.readCharacterByTypeAndRole("Geo", "Support"));
    }

    @Test
    void testFindCharactersByRegionNameSuccess() {
        when(characterRepository.findByRegionName("Mondstadt")).thenReturn(List.of(sampleCharacter()));

        List<CharacterDto> result = characterService.findCharactersByRegionName("Mondstadt");

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindCharactersByRegionNameThrows() {
        when(characterRepository.findByRegionName("Unknown")).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> characterService.findCharactersByRegionName("Unknown"));
    }

    @Test
    void testUpdateCharacterWithCache() {
        Character character = sampleCharacter();
        CharacterDto newDto = sampleDto();
        newDto.setName("AetherUpdated");

        when(characterRepository.findByName("Aether")).thenReturn(Optional.of(character));
        when(characterCache.get("Aether")).thenReturn(Optional.of(sampleDto()));
        when(characterRepository.save(any())).thenReturn(character);

        Character result = characterService.updateCharacter("Aether", newDto);

        assertEquals("AetherUpdated", result.getName());
        verify(characterCache).put(eq("Aether"), any());
    }

    @Test
    void testUpdateCharacterThrows() {
        when(characterRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> characterService.updateCharacter("Unknown", sampleDto()));
    }

    @Test
    void testDeleteCharacterSuccess() {
        Character character = sampleCharacter();
        Relic relic = new Relic();
        relic.setCharacters(new HashSet<>(List.of(character)));

        character.setRecRelics(new HashSet<>(Set.of(relic)));
        Region region = new Region();
        region.setCharacters(new HashSet<>(Set.of(character)));
        character.setRegion(region);

        when(characterRepository.findByName("Aether")).thenReturn(Optional.of(character));

        characterService.deleteCharacter("Aether");

        verify(characterRepository).delete(character);
        verify(characterCache).remove("Aether");
    }

    @Test
    void testDeleteCharacterThrows() {
        when(characterRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> characterService.deleteCharacter("Unknown"));
    }

    @Test
    void testToDtoWithRelicsAndRegion() {
        Character character = sampleCharacter();

        Relic relic = new Relic();
        relic.setId(1L);
        relic.setName("Gladiator's Finale");
        relic.setType("Artifact");
        relic.setPcs2("ATK +18%");
        relic.setPcs4("Increases Normal Attack DMG");
        relic.setRarity(Rarity.Star5);
        relic.setCharacters(new HashSet<>());
        character.setRecRelics(Set.of(relic));

        Region region = new Region();
        region.setId(1L);
        region.setName("Mondstadt");
        region.setArchon("Barbatos");
        region.setCharacters(new HashSet<>());
        character.setRegion(region);

        // 🛠 СНАЧАЛА мокаем
        when(characterRepository.findAll()).thenReturn(List.of(character));

        // ✅ ПОТОМ вызываем метод
        List<CharacterDto> dtos = characterService.readCharacters();

        assertEquals(1, dtos.size());
        CharacterDto dto = dtos.get(0);

        assertEquals("Aether", dto.getName());
        assertFalse(dto.getRelic().isEmpty());
        assertFalse(dto.getRegion().isEmpty());

        RelicsDto relicDto = dto.getRelic().iterator().next();
        assertEquals("Gladiator's Finale", relicDto.getName());

        RegionDto regionDto = dto.getRegion().iterator().next();
        assertEquals("Mondstadt", regionDto.getName());
    }


    @Test
    void testToEntityWithRelicsAndRegion() {
        CharacterDto dto = sampleDto();

        RelicsDto relicDto = new RelicsDto();
        relicDto.setId(1L);
        relicDto.setName("Noblesse Oblige");
        relicDto.setType("Artifact");
        relicDto.setPcs2("Elemental Burst DMG +20%");
        relicDto.setPcs4("Party ATK buff");
        relicDto.setRarity(Rarity.Star5);

        dto.setRelic(Set.of(relicDto));

        RegionDto regionDto = new RegionDto();
        regionDto.setId(1L);
        regionDto.setName("Liyue");
        regionDto.setArchon("Morax");

        dto.setRegion(Set.of(regionDto));

        // Вызов приватного метода через createBulkCharacters
        when(characterRepository.saveAll(anyList())).thenAnswer(i -> i.getArguments()[0]);

        List<CharacterDto> result = characterService.createBulkCharacters(List.of(dto));

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateCharacterWithoutCache() {
        Character character = sampleCharacter();
        CharacterDto updatedDto = sampleDto();
        updatedDto.setName("UpdatedName");

        when(characterRepository.findByName("Aether")).thenReturn(Optional.of(character));
        when(characterCache.get("Aether")).thenReturn(Optional.empty());
        when(characterRepository.save(any())).thenReturn(character);

        Character result = characterService.updateCharacter("Aether", updatedDto);

        assertEquals("UpdatedName", result.getName());
        verify(characterCache, never()).put(anyString(), any()); // потому что не было кэша
    }

}
