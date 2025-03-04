package io.greitan.avion.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import io.greitan.avion.PlayerCorpses;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Logger {

    private static Component createMessage(String msg, NamedTextColor color) {
        return Component.text("[")
                .color(NamedTextColor.WHITE)
                .decorate(TextDecoration.BOLD)
                .append(Component.text("PlayerCorpses")
                        .color(NamedTextColor.LIGHT_PURPLE)
                        .decorate(TextDecoration.BOLD))
                .append(Component.text("] ")
                        .color(NamedTextColor.WHITE)
                        .decorate(TextDecoration.BOLD))
                .append(Component.text(msg).color(color).decorate(TextDecoration.BOLD));
    }

    public static void log(Component msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(msg);
    }

    public static void info(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(createMessage(msg, NamedTextColor.WHITE));
    }

    public static void warn(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(createMessage(msg, NamedTextColor.YELLOW));
    }

    public static void error(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(createMessage(msg, NamedTextColor.RED));
    }

    public static void error(Exception e) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        // Exception message
        console.sendMessage(createMessage("Exception: " + e.getMessage(), NamedTextColor.RED));

        // Stack trace logging
        for (StackTraceElement element : e.getStackTrace()) {
            Component stackTraceLine = Component.text("\tat " + element.toString())
                    .color(NamedTextColor.GRAY);
            console.sendMessage(stackTraceLine);
        }
    }

    public static void debug(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        boolean isDebug = PlayerCorpses.getInstance().getConfig().getBoolean("config.debug");

        if (isDebug) {
            console.sendMessage(createMessage(msg, NamedTextColor.BLUE));
        }
    }
}
