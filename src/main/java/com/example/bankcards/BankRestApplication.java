package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс приложения
 *
 * @author Smirnov Daniil
 */
@SpringBootApplication
public class BankRestApplication {

    /**
     * Точка входа в приложение. Запускает сервис для работы с картами.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(BankRestApplication.class, args);
    }
}
