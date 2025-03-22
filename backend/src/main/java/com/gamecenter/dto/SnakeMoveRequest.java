package com.gamecenter.dto;

public record SnakeMoveRequest(
    String gameId,
    String direction
) {}