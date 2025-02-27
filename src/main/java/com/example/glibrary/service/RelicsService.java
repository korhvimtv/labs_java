package com.example.glibrary.service;

import com.example.glibrary.model.GameRelics;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class RelicsService {

    private final List<GameRelics> relics = new ArrayList<>();

    public boolean addRelic(GameRelics relic) {
        relics.add(relic);
        return true;
    }

    public List<GameRelics> getAllRelics() {
        return relics;
    }

    public List<GameRelics> getRelicByName(String rName) {
        return relics.stream()
                .filter(relic -> relic.getRName().equalsIgnoreCase(rName))
                .toList();
    }

    public List<GameRelics> getRelicByTypeAndR2pcs(String rType, String r2pcs) {
        return relics.stream()
                .filter(relic -> relic.getRType().equalsIgnoreCase(rType)
                        && relic.getR2pcs().equalsIgnoreCase(r2pcs))
                .toList();
    }
}

