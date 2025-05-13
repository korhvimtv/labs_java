package com.example.glibrary;

import com.example.glibrary.service.VisitCounterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisitCounterServiceTest {

    private VisitCounterService visitCounterService;

    @BeforeEach
    void setUp() {
        visitCounterService = new VisitCounterService();
    }

    @Test
    void testIncrementCharacterVisit_NewCharacter() {
        int count = visitCounterService.incrementCharacterVisit("Diluc");
        assertEquals(1, count);
    }

    @Test
    void testIncrementCharacterVisit_ExistingCharacter() {
        visitCounterService.incrementCharacterVisit("Diluc");
        int count = visitCounterService.incrementCharacterVisit("Diluc");
        assertEquals(2, count);
    }

    @Test
    void testGetCharacterVisitCount_ExistingCharacter() {
        visitCounterService.incrementCharacterVisit("Keqing");
        visitCounterService.incrementCharacterVisit("Keqing");

        int count = visitCounterService.getCharacterVisitCount("Keqing");
        assertEquals(2, count);
    }

    @Test
    void testGetCharacterVisitCount_UnknownCharacter() {
        int count = visitCounterService.getCharacterVisitCount("Unknown");
        assertEquals(0, count);
    }
}
