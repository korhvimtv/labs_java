package com.example.glibrary.controller;

import com.example.glibrary.model.GameRelics;
import com.example.glibrary.service.RelicsService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relics")
public class RelicsController {

    private final RelicsService relicsService;

    @Autowired
    public RelicsController(RelicsService relicsService) {
        this.relicsService = relicsService;
    }

    @PostMapping
    public boolean addRelic(@RequestBody GameRelics relics) {
        return relicsService.addRelic(relics);
    }

    @GetMapping
    public List<GameRelics> getAllRelics() {
        return relicsService.getAllRelics();
    }

    @GetMapping("/{rName}")
    public List<GameRelics> getRelicsByName(@PathVariable String rName) {
        return relicsService.getRelicByName(rName);
    }

    @GetMapping("/search")
    public List<GameRelics> getRelicByTypeAndR2pcs (@RequestParam String rType,
                                                    @RequestParam String r2pcs) {
        System.out.println(rType + " " + r2pcs);
        return relicsService.getRelicByTypeAndR2pcs(rType, r2pcs);
    }
}

