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
import com.example.glibrary.repository.RegionRepository;
import com.example.glibrary.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private CharacterCache characterCache;

    @InjectMocks
    private RegionService regionService;

    private RegionDto regionDto;
    private Region region;
    private Character character;
    private CharacterDto characterDto;
    private RelicsDto relicsDto;
    private Map<String, RegionDto> cache;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize test data
        regionDto = new RegionDto();
        regionDto.setId(1L);
        regionDto.setName("TestRegion");
        regionDto.setArchon("TestArchon");

        region = new Region();
        region.setId(1L);
        region.setName("TestRegion");
        region.setArchon("TestArchon");
        region.setCharacters(new HashSet<>());

        character = new Character();
        character.setId(1L);
        character.setName("TestCharacter");
        character.setType("Fire");
        character.setRole("DPS");
        character.setWeapon("Sword");
        character.setRarity(Rarity.Star5);

        Relic relic = new Relic();
        relic.setId(1L);
        relic.setName("TestRelic");
        relic.setType("Attack");
        relic.setPcs2("2pc");
        relic.setPcs4("4pc");
        relic.setRarity(Rarity.Star5);
        character.setRecRelics(Set.of(relic));

        characterDto = new CharacterDto();
        characterDto.setId(1L);
        characterDto.setName("TestCharacter");
        characterDto.setType("Fire");
        characterDto.setRole("DPS");
        characterDto.setWeapon("Sword");
        characterDto.setRarity(Rarity.Star5);

        relicsDto = new RelicsDto();
        relicsDto.setId(1L);
        relicsDto.setName("TestRelic");
        relicsDto.setType("Attack");
        relicsDto.setPcs2("2pc");
        relicsDto.setPcs4("4pc");
        relicsDto.setRarity(Rarity.Star5);
        characterDto.setRelic(Set.of(relicsDto));

        Field cacheField = RegionService.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        cache = (ConcurrentHashMap<String, RegionDto>) cacheField.get(regionService);
    }

    @Test
    void createRegion_success() {
        when(regionRepository.save(any(Region.class))).thenReturn(region);

        Region result = regionService.createRegion(regionDto);

        assertNotNull(result);
        assertEquals(regionDto.getName(), result.getName());
        assertEquals(regionDto.getArchon(), result.getArchon());
        verify(regionRepository).save(any(Region.class));
    }

    @Test
    void readRegions_success() {
        when(regionRepository.findAll()).thenReturn(List.of(region));

        List<RegionDto> result = regionService.readRegions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(region.getName(), result.get(0).getName());
        verify(regionRepository).findAll();
    }

    @Test
    void readRegions_emptyList_throwsNotFoundException() {
        when(regionRepository.findAll()).thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> regionService.readRegions());
        assertEquals("List of Regions is empty", exception.getMessage());
        verify(regionRepository).findAll();
    }

    @Test
    void readRegionByName_cached_success() {
        cache.put("TestRegion", regionDto);

        RegionDto result = regionService.readRegionByName("TestRegion");

        assertNotNull(result);
        assertEquals(regionDto.getName(), result.getName());
        verify(regionRepository, never()).findByName(anyString());
    }

    @Test
    void readRegionByName_notCached_success() {
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.of(region));

        RegionDto result = regionService.readRegionByName("TestRegion");

        assertNotNull(result);
        RegionDto expected = new RegionDto();
        expected.setId(1L);
        expected.setName("TestRegion");
        expected.setArchon("TestArchon");
        expected.setCharacters(Collections.emptySet());
        assertEquals(expected, result);
        assertEquals(expected, cache.get("TestRegion"));
        verify(regionRepository).findByName("TestRegion");
    }

    @Test
    void readRegionByName_notFound_throwsNotFoundException() {
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> regionService.readRegionByName("TestRegion"));
        assertEquals("Region 'TestRegion' not found", exception.getMessage());
        verify(regionRepository).findByName("TestRegion");
    }

    @Test
    void updateRegion_success() {
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.of(region));
        when(regionRepository.save(any(Region.class))).thenReturn(region);

        Region result = regionService.updateRegion("TestRegion", regionDto);

        assertNotNull(result);
        assertEquals(regionDto.getName(), result.getName());
        assertEquals(regionDto.getArchon(), result.getArchon());
        verify(regionRepository).findByName("TestRegion");
        verify(regionRepository).save(any(Region.class));
    }

    @Test
    void updateRegion_notFound_throwsNotFoundException() {
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> regionService.updateRegion("TestRegion", regionDto));
        assertEquals("Region with name TestRegion not found", exception.getMessage());
        verify(regionRepository).findByName("TestRegion");
        verify(regionRepository, never()).save(any());
    }

    @Test
    void updateRegionCharacter_success() {
        when(characterRepository.findByName("TestCharacter")).thenReturn(Optional.of(character));
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.of(region));
        when(characterRepository.save(any(Character.class))).thenReturn(character);
        when(regionRepository.save(any(Region.class))).thenReturn(region);

        RegionDto result = regionService.updateRegionCharacter("TestCharacter", "TestRegion");

        assertNotNull(result);
        assertEquals(region.getName(), result.getName());
        assertEquals(1, result.getCharacters().size());
        verify(characterRepository).findByName("TestCharacter");
        verify(regionRepository).findByName("TestRegion");
        verify(characterRepository).save(any(Character.class));
        verify(regionRepository).save(any(Region.class));
        verify(characterCache).put(eq("TestCharacter"), any(CharacterDto.class));
    }

    @Test
    void updateRegionCharacter_characterNotFound_throwsNotFoundException() {
        when(characterRepository.findByName("TestCharacter")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> regionService.updateRegionCharacter("TestCharacter", "TestRegion"));
        assertEquals("Character with name TestCharacter not found", exception.getMessage());
        verify(characterRepository).findByName("TestCharacter");
        verify(regionRepository, never()).findByName(anyString());
    }

    @Test
    void updateRegionCharacter_regionNotFound_throwsNotFoundException() {
        when(characterRepository.findByName("TestCharacter")).thenReturn(Optional.of(character));
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> regionService.updateRegionCharacter("TestCharacter", "TestRegion"));
        assertEquals("Region with name TestRegion not found", exception.getMessage());
        verify(characterRepository).findByName("TestCharacter");
        verify(regionRepository).findByName("TestRegion");
    }

    @Test
    void deleteRegion_success_withCharacters() {
        region.getCharacters().add(character);
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.of(region));
        when(characterRepository.save(any(Character.class))).thenReturn(character);

        regionService.deleteRegion("TestRegion");

        assertTrue(region.getCharacters().isEmpty());
        verify(regionRepository).findByName("TestRegion");
        verify(characterRepository).save(character);
        verify(regionRepository).delete(region);
    }

    @Test
    void deleteRegion_success_noCharacters() {
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.of(region));

        regionService.deleteRegion("TestRegion");

        assertTrue(region.getCharacters().isEmpty());
        verify(regionRepository).findByName("TestRegion");
        verify(characterRepository, never()).save(any());
        verify(regionRepository).delete(region);
    }

    @Test
    void deleteRegion_notFound_throwsNotFoundException() {
        when(regionRepository.findByName("TestRegion")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> regionService.deleteRegion("TestRegion"));
        assertEquals("Region with name TestRegion not found", exception.getMessage());
        verify(regionRepository).findByName("TestRegion");
        verify(regionRepository, never()).delete(any());
    }

    @Test
    void toDto_withCharacters() {
        region.getCharacters().add(character);

        RegionDto result = regionService.toDto(region);

        assertNotNull(result);
        assertEquals(region.getId(), result.getId());
        assertEquals(region.getName(), result.getName());
        assertEquals(region.getArchon(), result.getArchon());
        assertEquals(1, result.getCharacters().size());
        assertEquals(character.getName(), result.getCharacters().iterator().next().getName());
    }

    @Test
    void toDto_noCharacters() {
        region.setCharacters(null);

        RegionDto result = regionService.toDto(region);

        assertNotNull(result);
        assertEquals(region.getId(), result.getId());
        assertEquals(region.getName(), result.getName());
        assertEquals(region.getArchon(), result.getArchon());
        assertNull(result.getCharacters());
    }

    @Test
    void toCharacterDto_withRelicsAndRegion() {
        character.setRegion(region);

        CharacterDto result = regionService.toCharacterDto(character);

        assertNotNull(result);
        assertEquals(character.getId(), result.getId());
        assertEquals(character.getName(), result.getName());
        assertEquals(character.getType(), result.getType());
        assertEquals(character.getRole(), result.getRole());
        assertEquals(character.getWeapon(), result.getWeapon());
        assertEquals(character.getRarity(), result.getRarity());
        assertEquals(1, result.getRelic().size());
        assertEquals(1, result.getRegion().size());
    }

    @Test
    void toCharacterDto_noRelicsNoRegion() {
        character.setRecRelics(null);
        character.setRegion(null);

        CharacterDto result = regionService.toCharacterDto(character);

        assertNotNull(result);
        assertEquals(character.getId(), result.getId());
        assertEquals(character.getName(), result.getName());
        assertEquals(character.getType(), result.getType());
        assertEquals(character.getRole(), result.getRole());
        assertEquals(character.getWeapon(), result.getWeapon());
        assertEquals(character.getRarity(), result.getRarity());
        assertTrue(result.getRelic().isEmpty());
        assertTrue(result.getRegion().isEmpty());
    }
}