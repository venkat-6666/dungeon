package com.sai.DungeonExplorer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {
    private String message;
    private boolean existingPlayer;
    private int highScore;
}