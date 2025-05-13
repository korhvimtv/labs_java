package com.example.glibrary;

import com.example.glibrary.DTO.RelicsDto;
import com.example.glibrary.exception.NotFoundException;
import com.example.glibrary.model.Character;
import com.example.glibrary.model.Rarity;
import com.example.glibrary.model.Relic;
import com.example.glibrary.repository.CharacterRepository;
import com.example.glibrary.repository.RelicsRepository;
import com.example.glibrary.service.RelicsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelicsServiceTest {

    private RelicsRepository relicsRepository;
    private CharacterRepository characterRepository;
    private RelicsService relicsService;

    @BeforeEach
    void setUp() {
        relicsRepository = mock(RelicsRepository.class);
        characterRepository = mock(CharacterRepository.class);
        relicsService = new RelicsService(relicsRepository, characterRepository);
    }

    @Test
    void testCreateRelic() {
        RelicsDto dto = new RelicsDto();
        dto.setName("Gladiator's Finale");
        dto.setType("ATK");
        dto.setPcs2("ATK +18%");
        dto.setPcs4("Boost normal attacks");
        dto.setRarity(Rarity.Star5);

        Relic saved = new Relic();
        saved.setName(dto.getName());

        when(relicsRepository.save(any(Relic.class))).thenReturn(saved);

        Relic result = relicsService.createRelic(dto);

        assertEquals("Gladiator's Finale", result.getName());
    }

    @Test
    void testReadRelicsSuccess() {
        Relic relic = new Relic();
        relic.setId(1L);
        relic.setName("Noblesse Oblige");
        relic.setType("Support");
        relic.setRarity(Rarity.Star5);

        when(relicsRepository.findAll()).thenReturn(List.of(relic));

        List<RelicsDto> result = relicsService.readRelics();

        assertEquals(1, result.size());
        assertEquals("Noblesse Oblige", result.get(0).getName());
    }

    @Test
    void testReadRelicsEmptyThrowsException() {
        when(relicsRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> relicsService.readRelics());
    }

    @Test
    void testReadRelicsByNameUsesCache() {
        Relic relic = new Relic();
        relic.setName("Thundering Fury");

        when(relicsRepository.findByName("Thundering Fury")).thenReturn(Optional.of(relic));

        RelicsDto dto1 = relicsService.readRelicsByName("Thundering Fury");

        RelicsDto dto2 = relicsService.readRelicsByName("Thundering Fury");

        assertEquals(dto1.getName(), dto2.getName());
        verify(relicsRepository, times(1)).findByName("Thundering Fury");
    }

    @Test
    void testReadRelicsByNameNotFound() {
        when(relicsRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> relicsService.readRelicsByName("NonExistent"));
    }

    @Test
    void testUpdateRelicSuccess() {
        Relic relic = new Relic();
        relic.setName("Old");

        RelicsDto update = new RelicsDto();
        update.setName("Updated");
        update.setType("DEF");
        update.setPcs2("DEF +20%");
        update.setPcs4("Shield boost");
        update.setRarity(Rarity.Star4);

        when(relicsRepository.findByName("Old")).thenReturn(Optional.of(relic));
        when(relicsRepository.save(any())).thenReturn(relic);

        Relic result = relicsService.updateRelic("Old", update);

        assertEquals("Updated", result.getName());
        assertEquals("DEF", result.getType());
    }

    @Test
    void testUpdateRelicNotFound() {
        when(relicsRepository.findByName("Ghost")).thenReturn(Optional.empty());

        RelicsDto dto = new RelicsDto();
        dto.setName("Anything");

        assertThrows(NotFoundException.class, () -> relicsService.updateRelic("Ghost", dto));
    }

    @Test
    void testUpdateRelicCharacterSuccess() {
        Character character = new Character();
        character.setId(1L);
        character.setName("Xiao");
        character.setRecRelics(new HashSet<>());

        Relic relic = new Relic();
        relic.setName("Viridescent Venerer");
        relic.setCharacters(new HashSet<>());

        when(characterRepository.findByName("Xiao")).thenReturn(Optional.of(character));
        when(relicsRepository.findByName("Viridescent Venerer")).thenReturn(Optional.of(relic));

        when(relicsRepository.save(any(Relic.class))).thenReturn(relic);

        Relic result = relicsService.updateRelicCharacter("Viridescent Venerer", "Xiao");

        assertNotNull(result);
        assertTrue(result.getCharacters().contains(character));
        assertTrue(character.getRecRelics().contains(relic));
        verify(characterRepository).save(character);
        verify(relicsRepository).save(relic);
    }


    @Test
    void testUpdateRelicCharacterNotFound() {
        when(characterRepository.findByName("Aether")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> relicsService.updateRelicCharacter("AnyRelic", "Aether"));
    }

    @Test
    void testDeleteRelicSuccess() {
        Relic relic = new Relic();
        relic.setName("Deepwood Memories");

        Character character = new Character();
        character.setRecRelics(new HashSet<>(Set.of(relic)));

        relic.setCharacters(new HashSet<>(Set.of(character)));

        when(relicsRepository.findByName("Deepwood Memories")).thenReturn(Optional.of(relic));

        relicsService.deleteRelic("Deepwood Memories");

        assertFalse(character.getRecRelics().contains(relic));
        verify(relicsRepository).delete(relic);
    }

    @Test
    void testDeleteRelicNotFound() {
        when(relicsRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> relicsService.deleteRelic("Unknown"));
    }
}
