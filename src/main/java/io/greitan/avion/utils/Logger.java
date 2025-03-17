/*
 * Comments generated using 0xAlpha AI Comment Generator v1.4.1
 * Copyright (c) 2025 by 0xAlpha. All rights reserved.
 * This software is provided "as-is", without warranty of any kind, express or implied.
 */
package io.greitan.avion.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import io.greitan.avion.PlayerCorpses;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Logger {

    /**
     * Creates a formatted log message with a color.
     *
     * @param msg   The message to be logged.
     * @param color The color of the message text.
     * @return The formatted Component message.
     */
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

    /**
     * Logs a message to the console.
     * 
     * @param msg The message to log.
     */
    public static void log(Component msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(msg);
    }

    /**
     * Logs an informational message to the console.
     * 
     * @param msg The informational message to log.
     */
    public static void info(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(createMessage(msg, NamedTextColor.WHITE));
    }

    /**
     * Logs a warning message to the console.
     * 
     * @param msg The warning message to log.
     */
    public static void warn(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(createMessage(msg, NamedTextColor.YELLOW));
    }

    /**
     * Logs an error message to the console.
     * 
     * @param msg The error message to log.
     */
    public static void error(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(createMessage(msg, NamedTextColor.RED));
    }

    /**
     * Logs an exception with a detailed stack trace.
     * 
     * @param e The exception to log.
     */
    public static void error(Exception e) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        // Log the exception message
        console.sendMessage(createMessage("Exception: " + e.getMessage(), NamedTextColor.RED));

        // Log the stack trace
        for (StackTraceElement element : e.getStackTrace()) {
            Component stackTraceLine = Component.text("\tat " + element.toString())
                    .color(NamedTextColor.GRAY);
            console.sendMessage(stackTraceLine);
        }
    }

    /**
     * Logs a debug message to the console, if debugging is enabled.
     * 
     * @param msg The debug message to log.
     */
    public static void debug(String msg) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        boolean isDebug = PlayerCorpses.getInstance().getConfig().getBoolean("config.debug", false);

        if (isDebug) {
            console.sendMessage(createMessage(msg, NamedTextColor.BLUE));
        }
    }
}
