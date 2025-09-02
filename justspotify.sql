-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 02 Sep 2025 pada 04.24
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `justspotify`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `payments`
--

CREATE TABLE `payments` (
  `id` bigint(20) NOT NULL,
  `amount` double NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_expiry` datetime(6) DEFAULT NULL,
  `status` enum('COMPLETED','EXPIRED','FAILED','PENDING') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `payments`
--

INSERT INTO `payments` (`id`, `amount`, `created_at`, `otp`, `otp_expiry`, `status`, `updated_at`, `user_id`) VALUES
(1, 50000, '2025-09-01 06:25:56.000000', '426615', '2025-09-01 06:35:56.000000', 'COMPLETED', '2025-09-01 06:30:40.000000', 5),
(2, 50000, '2025-09-01 06:37:45.000000', '815184', '2025-09-01 06:47:45.000000', 'COMPLETED', '2025-09-01 06:40:00.000000', 6),
(3, 50000, '2025-09-01 06:41:38.000000', '588282', '2025-09-01 06:51:38.000000', 'COMPLETED', '2025-09-01 06:42:25.000000', 6),
(4, 11.05, '2025-09-01 07:08:46.000000', '933299', '2025-09-01 07:18:46.000000', 'COMPLETED', '2025-09-01 07:09:48.000000', 5),
(5, 300, '2025-09-01 07:11:24.000000', '979337', '2025-09-01 07:21:24.000000', 'PENDING', '2025-09-01 07:11:24.000000', 2),
(6, 1800, '2025-09-01 15:35:07.000000', '234466', '2025-09-01 15:45:07.000000', 'COMPLETED', '2025-09-01 15:35:55.000000', 2);

-- --------------------------------------------------------

--
-- Struktur dari tabel `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` enum('ROLE_ADMIN','ROLE_USER') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Struktur dari tabel `songs`
--

CREATE TABLE `songs` (
  `id` bigint(20) NOT NULL,
  `album` varchar(255) DEFAULT NULL,
  `artist` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `songs`
--

INSERT INTO `songs` (`id`, `album`, `artist`, `created_at`, `duration`, `file_path`, `genre`, `title`, `updated_at`, `image_url`) VALUES
(2, 'gaktau', 'ed sheeran', '2025-08-31 09:24:51.000000', 180, '/music/87f431eb-3e08-411a-b07c-021803cb08d0_Ed Sheeran - Shape of You (Official Music Video) [JGwWNGJdvx8].mp3', 'Pop', 'Ed sheeran shape of you', '2025-09-01 08:09:44.000000', 'https://upload.wikimedia.org/wikipedia/commons/c/c1/Ed_Sheeran-6886_%28cropped%29.jpg'),
(3, 'gaktau', 'adele', '2025-08-31 09:28:51.000000', 180, '/music/e3859b2c-89c6-42bd-ba04-a254308ed10a_Adele - Someone Like You (Official Music Video) [hLQl3WQQoQ0].mp3', 'Pop', 'adele - someone like you', '2025-08-31 09:28:51.000000', NULL),
(4, 'gaktau', 'michael jackson', '2025-08-31 09:31:03.000000', 180, '/music/bed2748d-8e94-4cc3-82c0-9216a4c54e44_Michael Jackson - Thriller (Official 4K Video) [sOnqjkJTMaA].mp3', 'Pop', 'thriller - michael jackson', '2025-08-31 09:31:03.000000', NULL),
(5, 'gatau', 'child o mine', '2025-08-31 10:10:11.000000', 120, '/music/cb9b13e9-b805-4ea6-ae25-3f6481804b94_Guns N\' Roses - Sweet Child O\' Mine (Official Music Video) [1w7OgIMMRc4].mp3', 'rock', 'Guns and roses', '2025-08-31 10:10:11.000000', NULL),
(6, 'adas', 'toby fox', '2025-09-01 04:14:50.000000', 123, '/music/e425a449-e2c0-46cc-99b2-49093e055b2d_mus_mettaton_ex.ogg', 'wwa', 'mettaton', '2025-09-01 04:14:50.000000', NULL),
(8, 'sas', 'toby fox', '2025-09-01 06:04:31.000000', 233, '/music/edabb264-ff49-4a85-8814-34a99b7a986a_mus_spider.ogg', 'asda', 'spider', '2025-09-01 06:04:31.000000', NULL),
(10, 'koplo', 'silvy aja', '2025-09-01 15:50:48.000000', 1, '/music/d8f978fd-4d97-4235-9deb-a27ffab45d59_BUKIT BERBUNGA (VERSI JADUL) FYP TIK TOK VIRAL - SILVY K X OM HIMAWAN HARLAH KE 6 SABILU TAUBAH.mp3', 'koplo', 'bukit berbunga', '2025-09-01 15:51:32.000000', 'https://i.ytimg.com/vi/c5UonnxpCZA/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLAJaSLUw-R5902KkB6LlIIhEaAXmQ');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `is_paid` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `username`, `is_paid`) VALUES
(1, 'admin@musicplatform.com', '$2a$10$iNM4NpRdfYEVNJ4qC3pwS.DgiqG7HnQT4uY8iXO3KV.wQYegM7Gpm', 'admin', NULL),
(2, 'user@musicplatform.com', '$2a$10$qeSBdQbLDbZtBCyFGxlsfemWqMNEHGeia8qHL7QTM7T4rRTUoEy.2', 'user', b'1'),
(3, 'asfari160904@gmail.com', '$2a$10$XqTCwjXa3CTxdvqlQLIZIO.ru8WBvQcGlFkUTaw7oApH0WYwE5G/6', 'asfari', NULL),
(4, 'raihandayo@gmail.com', '$2a$10$vQdGas4mJHidff1wEUaDvOrEhtQ0RXhWGm9EGNcQkcqOEoJAA5TrS', 'raihan', NULL),
(5, 'ubaytitans@gmail.com', '$2a$10$NyV.qmGwe69bHKYbarqB1e6Zd/7vl9jBN0.6xDIHniZZIH8CoRt9a', 'Ubay', b'1'),
(6, 'akurayhan29@gmail.com', '$2a$10$86nJYBaFU/LbKmd57VKjIO2EeezYSfRLLLp/UWac14Fq6L1XW3hbG', 'rayhanandika', b'1');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_favorites`
--

CREATE TABLE `user_favorites` (
  `user_id` bigint(20) NOT NULL,
  `song_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user_favorites`
--

INSERT INTO `user_favorites` (`user_id`, `song_id`) VALUES
(4, 2),
(4, 3),
(5, 8),
(6, 8);

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 2);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKj94hgy9v5fw1munb90tar2eje` (`user_id`);

--
-- Indeks untuk tabel `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `songs`
--
ALTER TABLE `songs`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- Indeks untuk tabel `user_favorites`
--
ALTER TABLE `user_favorites`
  ADD PRIMARY KEY (`user_id`,`song_id`),
  ADD KEY `FKd4xx2ogq37bauh3ddcb6ynmpe` (`song_id`);

--
-- Indeks untuk tabel `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `payments`
--
ALTER TABLE `payments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `songs`
--
ALTER TABLE `songs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `FKj94hgy9v5fw1munb90tar2eje` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Ketidakleluasaan untuk tabel `user_favorites`
--
ALTER TABLE `user_favorites`
  ADD CONSTRAINT `FK4sv7b9w9adr0fjnc4u10exlwm` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKd4xx2ogq37bauh3ddcb6ynmpe` FOREIGN KEY (`song_id`) REFERENCES `songs` (`id`);

--
-- Ketidakleluasaan untuk tabel `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
