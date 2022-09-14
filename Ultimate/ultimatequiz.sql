-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Aug 17, 2021 at 11:45 PM
-- Server version: 10.4.10-MariaDB
-- PHP Version: 7.4.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ultimatequiz`
--

-- --------------------------------------------------------

--
-- Table structure for table `ads`
--

DROP TABLE IF EXISTS `ads`;
CREATE TABLE IF NOT EXISTS `ads` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `admob_app_id` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `admob_banner` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `admob_interstitial` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `admob_native` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `admob_reward` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `facebook_banner` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `facebook_interstitial` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `facebook_native` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `facebook_reward` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `adcolony_app_id` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `adcolony_banner` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `adcolony_interstitial` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `adcolony_reward` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `startapp_app_id` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `banner_type` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `interstitial_type` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `video_type` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `ads`
--

INSERT INTO `ads` (`id`, `admob_app_id`, `admob_banner`, `admob_interstitial`, `admob_native`, `admob_reward`, `facebook_banner`, `facebook_interstitial`, `facebook_native`, `facebook_reward`, `adcolony_app_id`, `adcolony_banner`, `adcolony_interstitial`, `adcolony_reward`, `startapp_app_id`, `banner_type`, `interstitial_type`, `video_type`, `created_at`, `updated_at`) VALUES
(1, 'ca-app-pub-3940256099942544~3347511713', 'ca-app-pub-3940256099942544/6300978111', 'ca-app-pub-3940256099942544/1033173712', 'ca-app-pub-3940256099942544/2247696110', 'ca-app-pub-3940256099942544/5224354917', 'IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID', 'YOUR_PLACEMENT_ID', 'YOUR_PLACEMENT_ID', 'YOUR_PLACEMENT_ID', 'apped7bbaa4966d4d81bb', 'vz3a1c0fdb7b484efb83', 'vz7a3eb7aaaaaf48b0a5', 'vz3f40016809b64ecf85', '204027387', 'admob', 'admob', 'admob', '2021-05-07 00:00:00', '2021-06-07 11:31:35');

-- --------------------------------------------------------

--
-- Table structure for table `audio_questions`
--

DROP TABLE IF EXISTS `audio_questions`;
CREATE TABLE IF NOT EXISTS `audio_questions` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL,
  `subcategory_id` int(11) NOT NULL,
  `quiz_id` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  `seconds` int(11) NOT NULL,
  `hint` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `true_answer` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false1` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false2` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false3` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `question_audio_url` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `premium_or_not` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blockeds`
--

DROP TABLE IF EXISTS `blockeds`;
CREATE TABLE IF NOT EXISTS `blockeds` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
CREATE TABLE IF NOT EXISTS `categories` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_img` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `popular_or_not` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `categories_category_name_unique` (`category_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `completedquestions`
--

DROP TABLE IF EXISTS `completedquestions`;
CREATE TABLE IF NOT EXISTS `completedquestions` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `question_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `quiz_id` int(11) NOT NULL,
  `subcategory_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  `question_type` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `continuequizzes`
--

DROP TABLE IF EXISTS `continuequizzes`;
CREATE TABLE IF NOT EXISTS `continuequizzes` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `quiz_id` int(11) NOT NULL,
  `quiz_name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `quiz_image_url` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `subcategory_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dailies`
--

DROP TABLE IF EXISTS `dailies`;
CREATE TABLE IF NOT EXISTS `dailies` (
  `id` bigint(20) UNSIGNED NOT NULL DEFAULT 1,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_id` int(11) NOT NULL,
  `subcategory_id` int(11) NOT NULL,
  `quiz_id` int(11) NOT NULL,
  `image_url` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `failed_jobs`
--

DROP TABLE IF EXISTS `failed_jobs`;
CREATE TABLE IF NOT EXISTS `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uuid` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `connection` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `exception` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `image_questions`
--

DROP TABLE IF EXISTS `image_questions`;
CREATE TABLE IF NOT EXISTS `image_questions` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL,
  `subcategory_id` int(11) NOT NULL,
  `quiz_id` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  `seconds` int(11) NOT NULL,
  `hint` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `true_answer` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false1` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false2` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false3` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `question_image_url` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `premium_or_not` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `migrations`
--

DROP TABLE IF EXISTS `migrations`;
CREATE TABLE IF NOT EXISTS `migrations` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `migration` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(5, '2014_10_12_000000_create_users_table', 1),
(6, '2014_10_12_100000_create_password_resets_table', 1),
(7, '2019_08_19_000000_create_failed_jobs_table', 1),
(8, '2021_02_24_193146_create_players_table', 1),
(9, '2021_02_24_215848_create_settings_table', 2),
(10, '2021_03_01_140226_create_blockeds_table', 3),
(11, '2021_03_03_110142_create_refers_table', 4),
(12, '2021_03_16_133404_create_categories_table', 5),
(13, '2021_03_24_133935_create_subcategories_table', 6),
(14, '2021_03_24_135224_create_quizzes_table', 6),
(15, '2021_03_24_214809_create_continuequizzes_table', 6),
(16, '2021_03_27_221706_create_completedquestions_table', 7),
(17, '2021_03_27_231703_create_questions_table', 8),
(18, '2021_04_06_095940_create_dailies_table', 9),
(19, '2021_04_22_002738_create_image_questions_table', 10),
(20, '2021_04_22_002851_create_text_questions_table', 10),
(21, '2021_04_22_002921_create_audio_questions_table', 10),
(22, '2021_04_29_230640_create_withdraws_table', 11),
(23, '2021_04_30_023326_create_payment_methods_table', 12),
(24, '2021_05_04_021611_create_ads_table', 13),
(30, '2021_05_18_104606_create_premium_audio_questions_table', 14);

-- --------------------------------------------------------

--
-- Table structure for table `password_resets`
--

DROP TABLE IF EXISTS `password_resets`;
CREATE TABLE IF NOT EXISTS `password_resets` (
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  KEY `password_resets_email_index` (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payment_methods`
--

DROP TABLE IF EXISTS `payment_methods`;
CREATE TABLE IF NOT EXISTS `payment_methods` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `method` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
CREATE TABLE IF NOT EXISTS `players` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_or_phone` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actual_score` int(11) NOT NULL,
  `total_score` int(11) NOT NULL,
  `image_url` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `referral_code` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `coins` int(11) NOT NULL,
  `last_claim` datetime DEFAULT NULL,
  `login_method` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `device_id` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `facebook` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `twitter` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `instagram` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `earnings_withdrawed` double(8,2) NOT NULL,
  `earnings_actual` double(8,2) NOT NULL,
  `blocked` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `players_email_or_phone_unique` (`email_or_phone`),
  UNIQUE KEY `players_referral_code_unique` (`referral_code`),
  UNIQUE KEY `players_device_id_unique` (`device_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `quizzes`
--

DROP TABLE IF EXISTS `quizzes`;
CREATE TABLE IF NOT EXISTS `quizzes` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_id` int(11) NOT NULL,
  `subcategory_id` int(11) NOT NULL,
  `image_url` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `refers`
--

DROP TABLE IF EXISTS `refers`;
CREATE TABLE IF NOT EXISTS `refers` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `player_email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `referred_source_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
CREATE TABLE IF NOT EXISTS `settings` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `currency` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `api_secret_key` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `min_to_withdraw` double(8,2) NOT NULL,
  `conversion_rate` int(11) NOT NULL,
  `hint_coins` int(11) NOT NULL,
  `referral_register_points` int(11) NOT NULL,
  `video_ad_coins` int(11) NOT NULL,
  `daily_reward` int(11) NOT NULL,
  `lang` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `one_device` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fifty_fifty` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `completed_option` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`id`, `currency`, `api_secret_key`, `min_to_withdraw`, `conversion_rate`, `hint_coins`, `referral_register_points`, `video_ad_coins`, `daily_reward`, `lang`, `one_device`, `fifty_fifty`, `completed_option`, `created_at`, `updated_at`) VALUES
(1, '$', 'todocode', 1.00, 1000, 50, 50, 200, 200, 'en', 'no', 'yes', 'yes', NULL, '2021-08-17 21:57:20');

-- --------------------------------------------------------

--
-- Table structure for table `subcategories`
--

DROP TABLE IF EXISTS `subcategories`;
CREATE TABLE IF NOT EXISTS `subcategories` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_id` int(11) NOT NULL,
  `image_url` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `text_questions`
--

DROP TABLE IF EXISTS `text_questions`;
CREATE TABLE IF NOT EXISTS `text_questions` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL,
  `subcategory_id` int(11) NOT NULL,
  `quiz_id` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  `seconds` int(11) NOT NULL,
  `hint` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `true_answer` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false1` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false2` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `false3` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `question_text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `premium_or_not` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_unique` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `email_verified_at`, `password`, `remember_token`, `created_at`, `updated_at`) VALUES
(1, 'Admin', 'admin@admin.com', NULL, '$2y$12$40gh8vnGdZB74lEc1NBoNexJ59lySRSgjOGAljFFmlGmBqExY1nce', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `withdraws`
--

DROP TABLE IF EXISTS `withdraws`;
CREATE TABLE IF NOT EXISTS `withdraws` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `player_email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` double(8,2) NOT NULL,
  `points` int(11) NOT NULL,
  `status` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payment_method` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payment_info` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
