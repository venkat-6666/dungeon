package com.sai.DungeonExplorer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDto {
    public String message;
    public int health;
    public int gold;
    public int potions;
    public boolean hasKey;
}