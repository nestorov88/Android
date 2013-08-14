-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Време на генериране: 
-- Версия на сървъра: 5.5.32
-- Версия на PHP: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- БД: `hangman`
--
CREATE DATABASE IF NOT EXISTS `hangman` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `hangman`;

-- --------------------------------------------------------

--
-- Структура на таблица `categories`
--

CREATE TABLE IF NOT EXISTS `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Схема на данните от таблица `categories`
--

INSERT INTO `categories` (`id`, `category_name`) VALUES
(1, 'Cities'),
(2, 'Other'),
(3, 'Animals');

-- --------------------------------------------------------

--
-- Структура на таблица `games`
--

CREATE TABLE IF NOT EXISTS `games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `word_id` int(11) NOT NULL,
  `result` bit(1) NOT NULL,
  `whole_word_guess` bit(1) NOT NULL,
  `guested_letters` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `word_id` (`word_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=16 ;

--
-- Схема на данните от таблица `games`
--

INSERT INTO `games` (`id`, `user_id`, `word_id`, `result`, `whole_word_guess`, `guested_letters`) VALUES
(1, 2, 76, b'1', b'1', NULL);

-- --------------------------------------------------------

--
-- Структура на таблица `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=28 ;

--
-- Схема на данните от таблица `users`
--

INSERT INTO `users` (`id`, `email`) VALUES
(2, 'ixidor@abv.bg'),
(1, 'nestorov88@gmail.com');

-- --------------------------------------------------------

--
-- Структура на таблица `words`
--

CREATE TABLE IF NOT EXISTS `words` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(25) NOT NULL,
  `category_id` int(11) NOT NULL,
  `added_by_user_id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `added_by_user_id` (`added_by_user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=96 ;

--
-- Схема на данните от таблица `words`
--

INSERT INTO `words` (`id`, `word`, `category_id`, `added_by_user_id`, `description`) VALUES
(61, 'Sofia', 1, 1, 'Capital of Bulgaria'),
(62, 'Moscow', 1, 1, 'Capital of Russia'),
(63, 'Berlin', 1, 1, 'Capital of Germany'),
(64, 'Paris', 1, 1, 'Capital of France'),
(65, 'Madrid', 1, 1, 'Capital of Spain'),
(66, 'Athens', 1, 1, 'Capital of Greece'),
(67, 'Budapest', 1, 1, 'Capital of Hungary'),
(68, 'Dublin', 1, 1, 'Capital of Ireland'),
(69, 'Rome', 1, 1, 'Capital of Italy'),
(70, 'Tokyo', 1, 1, 'Capital of Japan'),
(71, 'Lion', 3, 1, 'The king'),
(72, 'Wofl', 3, 1, 'Winter is comming'),
(73, 'Elephan', 3, 1, 'Very big animal that lives in africa'),
(74, 'Rabbit', 3, 1, 'White and small animal'),
(75, 'Tiger', 3, 1, 'Have big claws and stripes, lives in the jungle.'),
(76, 'Monkey', 3, 1, 'Our ancestor.'),
(77, 'Giraffe', 3, 1, 'Have very long neck'),
(78, 'Bear', 3, 1, 'Can be seen in the wood or near the river trying to catch some fish');

--
-- Ограничения за дъмпнати таблици
--

--
-- Ограничения за таблица `games`
--
ALTER TABLE `games`
  ADD CONSTRAINT `games_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `games_ibfk_2` FOREIGN KEY (`word_id`) REFERENCES `words` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения за таблица `words`
--
ALTER TABLE `words`
  ADD CONSTRAINT `words_ibfk_2` FOREIGN KEY (`added_by_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `words_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
