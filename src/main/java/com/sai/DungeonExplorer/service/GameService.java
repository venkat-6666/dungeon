package com.sai.DungeonExplorer.service;

import com.sai.DungeonExplorer.dto.GameResponseDto;
import com.sai.DungeonExplorer.dto.RegisterResponseDto;
import com.sai.DungeonExplorer.entity.Player;
import com.sai.DungeonExplorer.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameService {

    @Autowired
    private PlayerRepository repo;

    private Player currentPlayer;

    private int health;
    private int gold;
    private int potions;
    private boolean hasKey;
    private boolean gameOver;

    private final Random random = new Random();

    public RegisterResponseDto registerPlayer(String name) {
        Player player = repo.findByName(name).orElse(null);

        RegisterResponseDto response = new RegisterResponseDto();

        if (player == null) {
            player = new Player();
            player.setName(name);
            player.setBestScore(0);
            repo.save(player);

            response.setMessage("New player created!");
            response.setExistingPlayer(false);
            response.setHighScore(0);
        } else {
            response.setMessage("Welcome back!");
            response.setExistingPlayer(true);
            response.setHighScore(player.getBestScore());
        }

        resetGame();
        currentPlayer = player;

        return response;
    }

    private void resetGame() {
        health = 100;
        gold = 0;
        potions = 0;
        hasKey = false;
        gameOver = false;
    }

    private boolean checkGameOver() {
        if (health <= 0) {
            health = 0;
            currentPlayer.setGamesPlayed(currentPlayer.getGamesPlayed() + 1);
            currentPlayer.setLastScore(0);
            repo.save(currentPlayer);
            gameOver = true;
            return true;
        }
        return false;
    }

    public GameResponseDto move(String direction) {
        if (checkGameOver() || gameOver) {
            return new GameResponseDto("❌ Game Over. Start a new game.", health, gold, potions, hasKey);
        }

        int encounter = random.nextInt(20);

        if (encounter < 5) return findGold();
        if (encounter < 11) return monsterFight();
        if (encounter < 13) return findPotion();
        if (encounter == 13) return findKey();
        if (encounter <= 16) return trap();

        return new GameResponseDto("You walk through an empty corridor...", health, gold, potions, hasKey);
    }

    private GameResponseDto findGold() {
        int g = random.nextInt(20) + 5;
        gold += g;
        return new GameResponseDto("💰 You found " + g + " gold!", health, gold, potions, hasKey);
    }

    private GameResponseDto monsterFight() {
        if (random.nextBoolean()) {
            int dmg = random.nextInt(20) + 10;
            health -= dmg;

            if (checkGameOver()) {
                return new GameResponseDto("💀 You were killed by a monster!", health, gold, potions, hasKey);
            }

            return new GameResponseDto("💥 Monster hit you for " + dmg, health, gold, potions, hasKey);
        } else {
            health = Math.min(health + 5, 100);
            return new GameResponseDto("⚔ Monster defeated! +5 HP", health, gold, potions, hasKey);
        }
    }

    private GameResponseDto findPotion() {
        potions++;
        return new GameResponseDto("🧪 You found a potion!", health, gold, potions, hasKey);
    }

    private GameResponseDto findKey() {
        hasKey = true;
        return new GameResponseDto("🔑 You found the golden key!", health, gold, potions, hasKey);
    }

    private GameResponseDto trap() {
        int dmg = random.nextInt(15) + 5;
        health -= dmg;

        if (checkGameOver()) {
            return new GameResponseDto("💀 Trap killed you!", health, gold, potions, hasKey);
        }

        return new GameResponseDto("⚠ Trap! -" + dmg + " HP", health, gold, potions, hasKey);
    }

    public GameResponseDto usePotion() {
        if (checkGameOver() || gameOver) {
            return new GameResponseDto("❌ Game Over.", health, gold, potions, hasKey);
        }
        if (potions > 0) {
            potions--;
            health = Math.min(health + 30, 100);
            return new GameResponseDto("💉 Potion used! +30 HP", health, gold, potions, hasKey);
        }

        return new GameResponseDto("No potions left!", health, gold, potions, hasKey);
    }

    public GameResponseDto escape() {
        if (checkGameOver() || gameOver) {
            return new GameResponseDto("❌ Game already ended.", health, gold, potions, hasKey);
        }

        if (!hasKey) {
            return new GameResponseDto("🚪 Exit locked! You need a key.", health, gold, potions, hasKey);
        }

        gameOver = true;

        int score = (health <= 0) ? 0 : gold * health;
        String msg = "🏆 You escaped! Score = " + score;

        currentPlayer.setLastScore(score);
        currentPlayer.setGamesPlayed(currentPlayer.getGamesPlayed() + 1);

        if (score > currentPlayer.getBestScore()) {
            currentPlayer.setBestScore(score);
            msg += " 🎉 NEW HIGH SCORE!";
        }

        repo.save(currentPlayer);

        return new GameResponseDto(msg, health, gold, potions, hasKey);

    }

    public GameResponseDto status() {
        return new GameResponseDto("Status", health, gold, potions, hasKey);
    }
}
