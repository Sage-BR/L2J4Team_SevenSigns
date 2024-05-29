/*
 * This file is part of the L2J 4Team project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2j.gameserver.model.skill;

/**
 * Abnormal Visual Effect enumerated.
 * @author NosBit
 */
public enum AbnormalVisualEffect
{
	DOT_BLEEDING(1),
	DOT_POISON(2),
	DOT_FIRE(3),
	DOT_WATER(4),
	DOT_WIND(5),
	DOT_SOIL(6),
	STUN(7),
	SLEEP(8),
	SILENCE(9),
	ROOT(10),
	PARALYZE(11),
	FLESH_STONE(12),
	DOT_MP(13),
	BIG_HEAD(14),
	DOT_FIRE_AREA(15),
	CHANGE_TEXTURE(16),
	BIG_BODY(17),
	FLOATING_ROOT(18),
	DANCE_ROOT(19),
	GHOST_STUN(20),
	STEALTH(21),
	SEIZURE1(22),
	SEIZURE2(23),
	MAGIC_SQUARE(24),
	FREEZING(25),
	SHAKE(26),
	ULTIMATE_DEFENCE(28),
	VP_UP(29),
	VP_KEEP(29), // TODO: Unknown ClientID
	REAL_TARGET(30),
	DEATH_MARK(31),
	TURN_FLEE(32),
	INVINCIBILITY(33),
	AIR_BATTLE_SLOW(34),
	AIR_BATTLE_ROOT(35),
	CHANGE_WP(36),
	CHANGE_HAIR_G(37), // Gold Afro
	CHANGE_HAIR_P(38), // Pink Afro
	CHANGE_HAIR_B(39), // Black Afro
	UNKNOWN_40(40),
	STIGMA_OF_SILEN(41),
	SPEED_DOWN(42),
	FROZEN_PILLAR(43),
	CHANGE_VES_S(44),
	CHANGE_VES_C(45),
	CHANGE_VES_D(46),
	TIME_BOMB(47),
	MP_SHIELD(48),
	AIRBIND(49),
	CHANGEBODY(50),
	KNOCKDOWN(51),
	NAVIT_ADVENT(52),
	KNOCKBACK(53),
	CHANGE_7ANNIVERSARY(54),
	ON_SPOT_MOVEMENT(55),
	DEPORT(56),
	AURA_BUFF(57),
	AURA_BUFF_SELF(58),
	AURA_DEBUFF(59),
	AURA_DEBUFF_SELF(60),
	HURRICANE(61),
	HURRICANE_SELF(62),
	BLACK_MARK(63),
	BR_SOUL_AVATAR(64),
	CHANGE_GRADE_B(65),
	BR_BEAM_SWORD_ONEHAND(66),
	BR_BEAM_SWORD_DUAL(67),
	NO_CHAT(68),
	HERB_PA_UP(69),
	HERB_MA_UP(70),
	SEED_TALISMAN1(71),
	SEED_TALISMAN2(72),
	SEED_TALISMAN3(73),
	SEED_TALISMAN4(74),
	SEED_TALISMAN5(75),
	SEED_TALISMAN6(76),
	CURIOUS_HOUSE(77),
	NGRADE_CHANGE(78),
	DGRADE_CHANGE(79),
	CGRADE_CHANGE(80),
	BGRADE_CHANGE(81),
	AGRADE_CHANGE(82),
	SWEET_ICE_FLAKES(83),
	FANTASY_ICE_FLAKES(84),
	CHANGE_XMAS(85),
	CARD_PC_DECO(86),
	CHANGE_DINOS(87),
	CHANGE_VALENTINE(88),
	CHOCOLATE(89),
	CANDY(90),
	COOKIE(91),
	STARS_0(92),
	STARS_1(93),
	STARS_2(94),
	STARS_3(95),
	STARS_4(96),
	STARS_5(97),
	DUELING(98),
	FREEZING2(99),
	CHANGE_YOGI(100),
	YOGI(101),
	MUSICAL_NOTE_YELLOW(102),
	MUSICAL_NOTE_BLUE(103),
	MUSICAL_NOTE_GREEN(104),
	TENTH_ANNIVERSARY(105),
	XMAS_SOCKS(106),
	XMAS_TREE(107),
	XMAS_SNOWMAN(108),
	OTHELL_ROGUE_BLUFF(109),
	HE_PROTECT(110),
	SU_SUMCROSS(111),
	WIND_STUN(112),
	STORM_SIGN2(113),
	STORM_SIGN1(114),
	WIND_BLEND(115),
	DECEPTIVE_BLINK(116),
	WIND_HIDE(117),
	PSY_POWER(118),
	SQUALL(119),
	WIND_ILLUSION(120),
	SAYHA_FURY(121),
	AVE_GRAVITY_SPACE_1(122),
	HIDE4(123), // ave_gravity_space2
	PMENTAL_TRAIL(124),
	HOLD_LIGHTING(125),
	GRAVITY_SPACE_3(126),
	SPACEREF(127),
	HE_ASPECT(128),
	RUNWAY_ARMOR1(129),
	RUNWAY_ARMOR2(130),
	RUNWAY_ARMOR3(131),
	RUNWAY_ARMOR4(132),
	RUNWAY_ARMOR5(133),
	RUNWAY_ARMOR6(134),
	RUNWAY_WEAPON1(135),
	RUNWAY_WEAPON2(136),
	AVE_RUNWAY_WEAPON_1(137),
	AVE_RUNWAY_WEAPON_2(138),
	AVE_RUNWAY_WEAPON_3(139),
	AVE_RUNWAY_WEAPON_4(140),
	PALADIN_PROTECTION(141),
	SENTINEL_PROTECTION(142),
	REAL_TARGET_2(143),
	DIVINITY(144),
	SHILLIEN_PROTECTION(145),
	EVENT_STARS_0(146),
	EVENT_STARS_1(147),
	EVENT_STARS_2(148),
	EVENT_STARS_3(149),
	EVENT_STARS_4(150),
	EVENT_STARS_5(151),
	ABSORB_SHIELD(152),
	PHOENIX_AURA(153),
	REVENGE_AURA(154),
	EVAS_AURA(155),
	TEMPLAR_AURA(156),
	LONG_BLOW(157),
	WIDE_SWORD(158),
	BIG_FIST(159),
	SHADOW_STEP(160),
	TORNADO(161),
	SNOW_SLOW(162),
	SNOW_HOLD(163),
	UNKNOWN_164(164),
	TORNADO_SLOW(165),
	ASTATINE_WATER(166),
	BIG_BODY_COMBINATION_CAT_NPC(167),
	BIG_BODY_COMBINATION_UNICORN_NPC(168),
	BIG_BODY_COMBINATION_DEMON_NPC(169),
	BIG_BODY_COMBINATION_CAT_PC(170),
	BIG_BODY_COMBINATION_UNICORN_PC(171),
	BIG_BODY_COMBINATION_DEMON_PC(172),
	BIG_BODY_2(173),
	BIG_BODY_3(174),
	PIRATE_SUIT(175),
	DARK_ASSASSIN_SUIT(176),
	WHITE_ASSASSIN_SUIT(177),
	UNKNOWN_178(178),
	RED_WIZARD_SUIT(179),
	MYSTIC_SUIT(180),
	AVE_DRAGON_ULTIMATE(181),
	HALLOWEEN_SUIT(182),
	INFINITE_SHIELD1_AVE(183),
	INFINITE_SHIELD2_AVE(184),
	INFINITE_SHIELD3_AVE(185),
	INFINITE_SHIELD4_AVE(186),
	AVE_ABSORB2_SHIELD(187),
	UNKNOWN_188(188),
	UNKNOWN_189(189),
	TALISMAN_BAIUM(190),
	BLUE_DYNASTY(191),
	RED_ZUBEI(192),
	CHANGESHAPE_TRANSFORM(193),
	ANGRY_GOLEM_AVE(194),
	WA_UNBREAKABLE_SONIC_AVE(195),
	HEROIC_HOLY_AVE(196),
	HEROIC_SILENCE_AVE(197),
	HEROIC_FEAR_AVE_1(198),
	HEROIC_FEAR_AVE_2(199),
	AVE_BROOCH(200),
	AVE_BROOCH_B(201),
	AVE_BROOCH_C(202),
	AVE_BROOCH_D(203),
	AVE_BROOCH_E(204),
	AVE_BROOCH_F(205),
	INFINITE_SHIELD4_AVE_2(206),
	CHANGESHAPE_TRANSFORM_1(207),
	CHANGESHAPE_TRANSFORM_2(208),
	CHANGESHAPE_TRANSFORM_3(209),
	CHANGESHAPE_TRANSFORM_4(210),
	AVE_RED_LIBRA_1(211),
	AVE_RED_LIBRA_2(212),
	AVE_RED_LIBRA_3(213),
	AVE_RED_LIBRA_4(214),
	RO_COUNTER_TRASPIE(215),
	DISARMORED(216),
	RO_GHOST_REFLECT(217),
	CHANGESHAPE_TRANSFORM_5(218),
	ICE_ELEMENTALDESTROY(219),
	DORMANT_USER(220),
	NUWBIE_USER(221),
	THIRTEENTH_BUFF(222),
	SOCIAL_BOW(223),
	ARENA_UNSEAL_A(224),
	ARENA_UNSEAL_B(225),
	ARENA_UNSEAL_C(226),
	ARENA_UNSEAL_D(227),
	ARENA_UNSEAL_E(228),
	IN_BATTLE_RHAPSODY(229),
	IN_A_DECAL(230),
	IN_B_DECAL(231),
	CHANGESHAPE_TRANSFORM_6(232),
	CHANGESHAPE_TRANSFORM_7(234),
	SPIRIT_KING_WIND_AVE_B(235),
	EARTH_KING_BARRIER1_AVE(236),
	EARTH_KING_BARRIER2_AVE(237),
	AVE_WATER_SCREEN(238),
	AVE_FIRE_SCREEN(239),
	AVE_WIND_SCREEN(240),
	AVE_EARTH_SCREEN(241),
	AVE_WEAKNESS_AIM(242),
	AVE_VAMPIRIC_AIM(243),
	AVE_BLINDNESS_AIM(244),
	FOCUS_SHIELD(247),
	RAISE_SHIELD(248),
	TRUE_VANGUARD(249),
	SHIELD_WALL(250),
	FOOT_TRAIL(251), // ave_redlibra_fastrun1
	LEGEND_DECO_HERO(252), // ave_olympiad_legend_effect
	CHANGESHAPE_TRANSFORM_8(253), // ave_cat_expbuff
	SPIRIT_KING_WIND_AVE(254), // ave_wind_ability_power2
	AVE_FRINTESSA_BOSS_BIG_BODY(255),
	U098_BUFF_TA_DECO(255),
	U098_RIGHT_DECO(255),
	U098_LEFT_DECO(255),
	ORFEN_ENERGY1_AVE(256),
	ORFEN_ENERGY2_AVE(257),
	ORFEN_ENERGY3_AVE(258),
	ORFEN_ENERGY4_AVE(259),
	ORFEN_ENERGY5_AVE(260),
	CHANGESHAPE_CAT(261),
	CHANGESHAPE_PANDA(262),
	UNHOLY_BARRIER_AVE(263),
	AVE_ARENA_OCC_TEAM_HARDIN(264),
	AVE_ARENA_OCC_TEAM_DEVIANNE(265),
	RUDOLF_A_AVE(266),
	RUDOLF_B_AVE(267),
	RUDOLF_C_AVE(268),
	OLYMPIAD_MEDAL_AVE(269),
	OLYMPIAD_SPORT_A_AVE(270),
	OLYMPIAD_SPORT_B_AVE(271),
	OLYMPIAD_SPORT_C_AVE(272),
	D_CHAOS_MATCH_DECO_SMALL(273),
	TRANS_DECO_R(274),
	TRANS_DECO_B(275),
	TRANS_DECO_Y(276),
	TRANS_DECO_G(277),
	TRANS_DECO_P(278),
	TRANS_DECO_P2(279),
	TRANS_DECO_W(280),
	TRANS_DECO_R2(281),
	TRANS_DECO_W2(282),
	HDDOWN_AVE(283),
	HDMDOWN_AVE(284),
	AVE_RAID_BERSERK(285),
	KISSNHEART_AVE(286),
	EARTH_BARRIER_AVE(287),
	LILITH_DARK_BARRIER_AVE(288),
	EARTH_BARRIER2_AVE(289),
	CROFFIN_QUEEN_INVINCIBILITY_AVE(290),
	MPDOWN_AVE(291),
	WORLDCUP_RED_AVE(292),
	WORLDCUP_BLUE_AVE(293),
	AVE_EV_SOCCER_BUFF_3(294),
	AVE_EV_SOCCER_BUFF_4(295),
	SURGEWAVE_AVE(296),
	BLESS_AVE(297),
	ANTHARAS_RAGE_AVE(298),
	AVE_EV_SUMMER_1(299),
	AVE_EV_SUMMER_2(300),
	AVE_SIN_EATER(301),
	G_BARRIER_AVE(302),
	AVE_ATTACK_ALL_ANGLE(303),
	FIREWORKS_001T(304),
	FIREWORKS_002T(305),
	FIREWORKS_003T(306),
	FIREWORKS_004T(307),
	FIREWORKS_005T(308),
	FIREWORKS_006T(309),
	FIREWORKS_007T(310),
	FIREWORKS_008T(311),
	FIREWORKS_009T(312),
	FIREWORKS_010T(313),
	FIREWORKS_011T(314),
	FIREWORKS_012T(315),
	FIREWORKS_013T(316),
	FIREWORKS_014T(317),
	FIREWORKS_015T(318),
	P_CAKE_AVE(319),
	INIT_TRANSFORM(320), // ave_curse_weapon
	AVE_ZARICHE(321),
	AVE_AKA_MANAFM(322),
	AVE_CURSE_BARRIER(323),
	ZARICHE_PRISION_AVE(323),
	RUDOLPH(324),
	XMAS_HEART_AVE(325),
	XMAS_HAND_AVE(326),
	LUCKYBAG_AVE(327),
	HEROIC_MIRACLE_AVE(328),
	POCKETPIG_AVE(329),
	BLACK_STANCE_AVE(330),
	BLACK_TRANS_DECO_AVE(330),
	KAMAEL_BLACK_TRANSFORM(331),
	WHITE_STANCE_AVE(332),
	HEAL_DECO_AVE(332),
	KAMAEL_WHITE_TRANSFORM(333),
	LONG_RAPIER_WHITE_AVE(334),
	LONG_RAPIER_BLACK_AVE(335),
	ZARICHE_PRISION2_AVE(336),
	BLUEHEART_AVE(337),
	ATTACK_BUFF_AVE(338),
	SHIELD_BUFF_AVE(339),
	BERSERKER_BUFF_AVE(340),
	SEED_TALISMAN8(341),
	H_GOLD_STAR1_AVE(342),
	H_GOLD_STAR2_AVE(343),
	H_GOLD_STAR3_AVE(344),
	H_GOLD_STAR4_AVE(345),
	H_GOLD_STAR5_AVE(346),
	AVE_RAID_AREA(347),
	H_HEART_ADEN_AVE(348),
	H_ADENBAG_COIN_AVE(349),
	KAMAEL_BLACK_TRANSFORM_2(350),
	KAMAEL_WHITE_TRANSFORM_2(351),
	H_DEATH_EFFECT_AVE(352),
	AVE_WHITE_KNIGHT(353),
	U_ER_WI_WINDHIDE_AVE(354),
	CHANGESHAPE_TED(355),
	AVE_RED_LIBRA_6(356),
	AVE_RED_LIBRA_7(357),
	AVE_RED_LIBRA_8(358),
	CHANGESHAPE_SAMURAI(359),
	CHANGESHAPE_KITTY_2(360),
	AVE_RED_LIBRA_11(361),
	AVE_RED_LIBRA_12(362),
	AVE_RED_LIBRA_13(363),
	CHANGESHAPE_NOBLE_BLACK(364),
	CHANGESHAPE_NOBLE_WHITE(365),
	CHANGESHAPE_MUSKETEER_BLUE(366),
	CHANGESHAPE_MUSKETEER_RED(367),
	AVE_RED_LIBRA_18(368),
	AVE_RED_LIBRA_19(369),
	CHANGESHAPE_COWBOY_RED(370),
	CHANGESHAPE_PRIEST(371),
	CHANGESHAPE_CALVARY(372),
	CHANGESHAPE_NKITTY(373),
	CHANGESHAPE_BARBARIAN(374),
	CHANGESHAPE_VALKYRIE(375),
	AVE_RED_LIBRA_26(376),
	AVE_RED_LIBRA_27(377),
	AVE_RED_LIBRA_28(378),
	AVE_RED_LIBRA_29(379),
	AVE_RED_LIBRA_30(380),
	AVE_RED_LIBRA_31(381),
	AVE_RED_LIBRA_32(382),
	AVE_RED_LIBRA_33(383),
	AVE_RED_LIBRA_34(384),
	RANKER_DECO_HUMAN(385), // ave_race_reward_type1
	RANKER_DECO_KAMAEL(386), // ave_race_reward_type2
	JDK_BODY_FIRE_1(387),
	DK_BONEPRISON_AVE(388),
	DK_CHANGE_ARMOR(389),
	DK_IGNITION_HUMAN_AVE(390),
	DK_IGNITION_ELF_AVE(391),
	DK_IGNITION_DARKELF_AVE(392),
	DK_ACCELERATION_AVE(393),
	DK_BURN_AVE(394),
	DK_FREEZING_AREA_AVE(395),
	DK_SHOCK_AVE(396),
	DK_PERFECT_SHILED_AVE(397),
	DK_FROSTBITE_AVE(398),
	DK_BONEPRISON_SQUELA_AVE(399),
	H_B_HASTE_B_AVE(400),
	FS_STIGMA_AVE(401),
	AVE_DEATHKNIGHT_CLOCK_RANKER(402),
	FORT_FLAG_AVE(403),
	H_EVENT_MOON_AVE(404),
	AVE_RED_LIBRA_35(405),
	AVE_RED_LIBRA_36(406),
	AVE_RED_LIBRA_37(407),
	AVE_RED_LIBRA_38(408),
	H_R_BARRIER_AVE(409),
	H_R_FIRE_DEBUFF_AVE(410),
	H_P_FIRE_DEBUFF_AVE(411),
	AVE_HALLOWEEN_MASK1(412),
	AVE_HALLOWEEN_MASK2(413),
	AVE_HALLOWEEN_MASK3(414),
	AVE_HALLOWEEN_MASK4(415),
	AVE_HALLOWEEN_MASK5(416),
	AVE_HALLOWEEN_MASK6(417),
	H_EVENT_PUMPKIN_AVE(418),
	H_EVENT_PUMPKIN_B_AVE(419),
	AVE_POISON_GROUND_G(420),
	AVE_POISON_GROUND_B(421),
	AVE_POISON_GROUND_P(422),
	AVE_POISON_GROUND_R(423),
	RANKER_DECO_ORC(424),
	H_DEBUFF_SELF_B_AVE(425),
	H_AURA_DEBUFF_B_AVE(426),
	H_ULTIMATE_DEFENCE_B_AVE(427),
	RANKER_DECO_ELF(428),
	RANKER_DECO_DARKELF(429),
	RANKER_DECO_DWARF(430),
	H_Y_MAGNETIC_AVE(431),
	FORCE_SIT(432),
	H_R_NATURAL_BEAST_AVE(433),
	AVE_ALMIGHTY_HEADS_1(434),
	H_BERSERKER_B_BUFF_AVE(435),
	H_BERSERKER_C_BUFF_AVE(436),
	U_AVE_DIVINITY(437),
	Y_RO_GHOST_REFLECT_AVE(438),
	S_EVENT_KITE_DECO(439),
	AVE_REFLECTION_ARMOR_2(440),
	H_B_SYMPHONY_SWORD_AVE(441),
	H_B_SYMPHONY_SWORD_DEFENCE_AVE(442),
	H_B_SYMPHONY_SWORD_BUFF_A_AVE(443),
	H_B_SYMPHONY_SWORD_BUFF_B_AVE(444),
	H_G_POISON_DANCE_AVE(445),
	H_G_POISON_DANCE_DEBUFF_A_AVE(446),
	H_G_POISON_DANCE_DEBUFF_B_AVE(447),
	H_R_POISON_DANCE_BUFF_B_AVE(448),
	AVE_MADNESS_WALTZ_DEBUFF(449),
	H_B_CHOCOLATE_AVE(450),
	H_P_CHAIN_BLOCK_AVE(451),
	H_EVENT_MASK_AVE(452),
	AVE_REDLIBRA_39(453),
	H_R_ORC_TITAN_AVE(454),
	H_R_GIGANTIC_WEAPON_AVE(455),
	AVE_ORC_TITAN_AIRBORNE(456),
	H_B_TOTEM_PUMA_AVE(457),
	H_Y_TOTEM_RABBIT_AVE(458),
	H_G_TOTEM_OGRE_AVE(459),
	H_Y_ORC_HP_AVE(460),
	H_B_ORC_HP_AVE(461),
	V_ORC_IMMOLATION_BODY_AVE(462),
	H_R_ORC_WAR_ROAR_AVE(463),
	AVE_ORC_ULT_3(464),
	V_ORC_TOUCH_AVE(465),
	H_B_PARTY_UNITEA_AVE(466),
	AVE_LAUGH(467),
	V_ORC_DEBUFF_MASTER_AVE(468),
	V_ORC_FLAME_BLAST_A_AVE(469),
	V_ORC_FLAME_BLAST_B_AVE(470),
	H_B_PARTY_UNITEB_AVE(471),
	H_R_BLOOD_LINKA_AVE(472),
	H_R_BLOOD_LINKB_AVE(473),
	V_ORC_COLD_FLAME_A_AVE(474),
	V_ORC_COLD_FLAME_B_AVE(475),
	H_B_SPIRITWIND_AVE(477),
	H_W_SEACREATURE_AVE(478),
	V_EVENT_2020_SUMMER_AVE(479),
	SHARK_COSTUME(480),
	H_SY_BOARD_RANKER_DECO(481),
	H_SY_BOARDD_DECO(482),
	H_SY_BOARDC_DECO(483),
	H_SY_BOARDB_DECO(484),
	H_SY_BOARDA_DECO(485),
	H_G_AFFINITYA_AVE(486),
	H_G_AFFINITYB_AVE(487),
	V_SY_ELE_GUARD_AVE(488),
	H_SY_ELEMENTAL_STORM_AVE(489),
	V_SY_ELE_TRANSITION_AVE(490),
	H_SY_ELEMENTAL_RECOVERY_AVE(491),
	H_SY_FREEZING_AVE(492),
	V_SY_BURST_TIME_AVE(493),
	UNKNOWN_494(494),
	S_MASS_SALVATION_BUFF_AVE(496),
	H_B_EXPEL_AVE(497),
	S_BANISH_DEBUFF_AVE(498),
	H_B_PROTECTION_SHIELD_AVE(499),
	H_R_PROTECTION_SHIELD_AVE(500),
	H_B_PROTECTION_MANA_AVE(501),
	H_Y_ROLLING_DICEA_AVE(502),
	H_Y_ROLLING_DICEB_AVE(503),
	H_Y_ROLLING_DICEC_AVE(504),
	H_R_METEOR_AVE(505),
	H_SY_ELEMENTAL_ROAR_AVE(507),
	WH_HEAL_AVE(508),
	H_SY_ELEMENTAL_DUST_AVE(509),
	S_MENTOR_AVE(510),
	RANKER_DECO_SYLPH(511),
	H_SY_ELEMENTAL_MARK_AVE(513),
	Z_ICE_SCREEN_F(514),
	V_ICE_ONEHAND(515),
	H_B_ICE_TWOHAND(516),
	V_ICE_LONG(517),
	V_FORTUNE_TIME(518),
	V_FORTUNE_TIME_DEBUFF(519),
	V_GOLD_STONE(520),
	V_GOLD_STONE_DEBUFF(521),
	V_VITAL_GAIN_LV1(522),
	V_VITAL_GAIN_LV2(523),
	V_VITAL_GAIN_LV3(524),
	V_VITAL_GAIN_LV4(525),
	V_VITAL_GAIN_LV5(526),
	H_B_OVERDRIVE(527),
	WINTER_COSTUME(528),
	H_LUCKYBAG2(529),
	V_LIDVIOR_BODY(530),
	V_DISRON_VICTORY(531),
	H_P_AEGIS_ARMOR_DECO(532),
	RETRO_DYNASTY(533),
	RETRO_MOIRAI(534),
	RETRO_VERSPER_NOBLE(535),
	RETRO_VORPAL(536),
	RETRO_DARK_CRYSTAL(537),
	RETRO_MAJESTIC(538),
	RETRO_NIGHTMARE(539),
	V_WORLDCASTLEWAR_HERO_WEAPON(540),
	V_EARASED(541),
	H_R_IMPRISON(542),
	H_R_NEMESIS(543),
	H_Y_PROTECTION_SHIELD(544),
	H_P_PROTECTION_SHIELD(545),
	H_W_PROTECTION_SHIELD(546),
	H_B_HEAVEN_SEAL(547),
	UNKNOWN_548(548),
	V_KNIGHT_PHOENIX(549),
	V_KNIGHT_HELL_HAND(550),
	V_KNIGHT_SHELTER_02(551),
	V_KNIGHT_SHELTER(552),
	V_KNIGHT_CONDEMN(553),
	H_Y_ULTIMATE_DEFENCE(554),
	DK2_EQUAL_AURA(555),
	DK2_RAGE_AURA(556),
	DK2_SARDONIC_FORTITUDEA(557),
	DK2_SARDONIC_FORTITUDEB(558),
	S_DRAGON_SLAYER(559),
	V_DARION_REFLECTION(560),
	V_DARION_ROAR(561),
	V_DARION_CUBIC(562),
	V_BERES_FOCUSING(563),
	V_BERES_CURE(564),
	V_ROGUE_PHANTOM_01(566),
	V_ROGUE_PHANTOM_02(567),
	V_ROGUE_PHANTOM_03(568),
	V_ROGUE_FURY_BLADE(569),
	V_ROGUE_SYNCHRO(570),
	H_P_REVERSE_PULLING(571),
	H_P_FAST_ASSAULT(574),
	H_R_BLUFF(575),
	V_ROGUE_CRITICAL_WOUND(576),
	H_P_PROTECTION_OF_DARK(578),
	H_P_PROTECTION_OF_EVA(579),
	DK2_ULTIMATE_DEFENCE(580),
	DK2_ROAR(581),
	H_AURA_DEBUFFB(582),
	AVE_AURA_DEBUFF(583),
	H_Y_SAGITTARIUSA_AVE(584),
	H_B_MOONLIGHTA_AVE(585),
	H_P_GHOSTA_AVE(586),
	H_R_TRICKSTERA_AVE(587),
	V_ARCHER_TRUE_TARGET_AVE(588),
	V_ARCHER_SHAPE_SHOOTER_AVE(589),
	H_R_SPIKE_SHOT_AVE(590),
	V_ARCHER_CHAIN_ARREST_AVE(591),
	V_ARCHER_ARROW_SHOWER_AVE(592),
	V_WIZARD_MYSTIC_SHIELD_AVE(594),
	V_WIZARD_SOUL_GARD_AVE(595),
	V_WIZARD_DEEP_SLEEP_AVE(596),
	S_EVENT_MOON_AVE(597),
	V_DARION_REFLECTION_AVE(598),
	V_OR_BURNING_BEAST_FIRE_AVE(599),
	VANGUARD_CHANGE_ARMOR(600),
	VANGUARD_RANKER(601),
	H_Y_BLESSING_OF_EMPEROR_AVE(602),
	H_B_BLESSING_OF_GUARDIAN_AVE(603),
	H_P_BLESSING_OF_LORD_AVE(604),
	H_B_LIFELINK_AVE(605),
	H_P_SERVITOR_ULTIMATE_AVE(606),
	AVE_STAR_X01_EV(607),
	AVE_STAR_X02_EV(608),
	AVE_STAR_X03_EV(609),
	AVE_STAR_X04_EV(610),
	AVE_STAR_X05_EV(611),
	AVE_STAR_X06_EV(612),
	AVE_STAR_X07_EV(613),
	AVE_STAR_X08_EV(614),
	AVE_STAR_X09_EV(615),
	AVE_STAR_X10_EV(616),
	AVE_STAR_X11_EV(617),
	AVE_STAR_X12_EV(618),
	AVE_STAR_X13_EV(619),
	AVE_STAR_X14_EV(620),
	AVE_STAR_X15_EV(621),
	AVE_STAR_X16_EV(622),
	AVE_STAR_X17_EV(623),
	AVE_STAR_X18_EV(624),
	V_PROTECTION_CONDITION_AVE(625),
	V_WAR_CONTENDER_AVE(626),
	V_WAR_SWORD_ART_BUFF_AVE(627),
	H_B_WIDE_SWEEP_AVE(628),
	H_B_SPEAR_PRISON_AVE(629),
	V_WAR_ANGER_BUFF_AVE(630),
	WAR_ROAR_SCREEN_AVE(631),
	OPAL_DEBUFF_BARRIER_AVE(632),
	V_OR_BURNING_BEAST_GROUND_AVE(633),
	V_BAIUM_LIGHTENING_SHIELD_AVE(634),
	H_R_REDSPRITE_SCREEN(635),
	H_B_FRAGARACH_AVE(636),
	H_P_DISARM_AVE(639),
	H_P_OVERWHELMINGA_AVE(640),
	H_P_OVERWHELMINGB_AVE(641),
	H_B_SOULSIGN_AVE(642),
	H_P_DEVIL_STANDING1_AVE(644),
	H_P_DEVIL_STANDING2_AVE(645),
	H_P_DEVIL_EQUIP_TA(646),
	H_R_FIRESWORD_EQUIP_TA(647),
	H_Y_TRANSFORM_CRUSADER_AVE(649),
	H_Y_TRANSFORM_CRUSADERA_AVE(650),
	H_Y_TRANSFORM_CRUSADERB_AVE(651),
	H_Y_TRANSFORM_CRUSADERC_AVE(652),
	H_P_UNIQUE_SILENCE_AVE(653),
	H_Y_STIGMA_OF_SODMZ_AVE(654),
	H_R_STIGMA_OF_SPTDAN_AVE(655),
	H_Y_AMADEUS_AVE(656),
	H_Y_AMADEUSB_AVE(657),
	H_R_FLAMENCO_AVE(658),
	H_R_FLAMENCOB_AVE(659),
	H_Y_MADNESS_BEATB_AVE(667),
	H_R_MADNESS_WALTZB_AVE(668),
	H_B_SONG_OF_SILENCE4_AVE(669),
	H_Y_DANCE_OF_MEDUSA_AVE(670),
	H_R_POISON_DANCE_AVE(672),
	H_R_POISON_DANCE_BUFFC_AVE(673),
	H_R_POISON_DANCE_DEBUFFA_AVE(674),
	H_R_POISON_DANCE_DEBUFFB_AVE(675),
	H_B_PROTECTION_SHIELDB_AVE(676),
	H_B_PROTECTION_SHIELDC_AVE(677),
	H_R_PROTECTION_SHIELDB_AVE(679),
	H_R_PROTECTION_SHIELDC_AVE(680),
	H_B_PROTECTION_MANAB_AVE(682),
	H_B_PROTECTION_MANAC_AVE(683),
	H_Y_PROTECTION_SHIELDB_AVE(688),
	H_Y_PROTECTION_SHIELDC_AVE(689),
	H_P_PROTECTION_SHIELDB_AVE(691),
	H_P_PROTECTION_SHIELDC_AVE(692),
	H_W_PROTECTION_SHIELDB_AVE(694),
	H_W_PROTECTION_SHIELDC_AVE(695),
	DANCING_ANIMATION(697),
	FAN_ANIMATION(698),
	H_B_OMAN_BOSS_BUFF1_AVE(699),
	H_P_OMAN_BOSS_BUFF2_AVE(700),
	H_R_OMAN_BOSS_BUFF3_AVE(701),
	H_R_PROTECTION_OF_DARK_AVE(702),
	HEAVEN_CLOTH_EQUIP_AVE(703),
	HEAVEN_CLOTH_TRIGGER_AVE(704),
	H_R_KELBIM_ASTATINE_AVE(710),
	H_Y_KELBIM_ASTATINE_AVE(711),
	H_B_KELBIM_ASTATINE_AVE(712),
	H_P_KELBIM_ASTATINE_AVE(713),
	H_W_KELBIM_ASTATINE_AVE(714),
	H_B_ICE_DUALSWORD_AVE(717),
	H_B_ULTIMATE_DEFENCE_AVE(719),
	H_B_ULTIMATE_DEFENCEB_AVE(720),
	H_R_ULTIMATE_DEFENCE_AVE(721),
	H_P_ULTIMATE_DEFENCE_AVE(722),
	H_B_SPEAR_PRISON_1E_AVE(724),
	H_B_SPEAR_PRISON_1E_AVE2(725),
	H_B_SPEAR_PRISON_3E_AVE(726),
	V_B_SOULSIGN_E1LV_AVE(727),
	V_B_SOULSIGN_E1LV_AVE2(728),
	V_ARCHER_FLAME_ARROW_E1LV_AVE(729),
	V_ARCHER_FLAME_ARROW_E1LV_AVE2(730),
	V_ARCHER_ARROW_SHOWER_E1LV_AVE(731),
	V_ARCHER_ARROW_SHOWER_E1LV_AVE2(732),
	V_ARCHER_PIERCING_ARROW_E1LV_AVE(733),
	V_ARCHER_PIERCING_ARROW_E1LV_AVE2(734),
	V_ARCHER_CHAIN_ARREST_E1LV_AVE(735),
	V_ARCHER_CHAIN_ARREST_E1LV_AVE2(736),
	V_B_NOCTURN_OF_EVA_E1LV_AVE(737),
	V_B_NOCTURN_OF_EVA_E1LV_AVE2(738),
	H_ASN_EROSION1A_AVE(740),
	H_ASN_EROSION2A_AVE(741),
	H_ASN_EROSION3A_AVE(742),
	H_ASN_EROSION1B_AVE(743),
	H_ASN_EROSION2B_AVE(744),
	H_ASN_EROSION3B_AVE(745),
	H_ASN_ENROACHMENT1A_AVE(746),
	H_ASN_ENROACHMENT2A_AVE(747),
	H_ASN_ENROACHMENT3A_AVE(748),
	H_ASN_ENROACHMENT1B_AVE(749),
	H_ASN_ENROACHMENT2B_AVE(750),
	H_ASN_ENROACHMENT3B_AVE(751),
	H_ASN_TIME_DISTORTION_AVE(753),
	H_ASN_SUPPRESSION_NOVICE_AVE(754),
	H_ASN_SUPPRESSION_WARRIOR_AVE(755),
	H_ASN_SUPPRESSION_KNIGHT_AVE(756),
	H_ASN_SUPPRESSION_BARD_AVE(757),
	H_ASN_SUPPRESSION_ROGUE_AVE(758),
	H_ASN_SUPPRESSION_ARCHER_AVE(759),
	H_ASN_SUPPRESSION_HUNTER_AVE(760),
	H_ASN_SUPPRESSION_DEATHKNIGHT_AVE(761),
	H_ASN_SUPPRESSION_ENCHANTER_AVE(762),
	H_ASN_SUPPRESSION_WIZARD_AVE(763),
	H_ASN_SUPPRESSION_SUMMONER_AVE(764),
	H_ASN_SUPPRESSION_HEALER_AVE(765),
	H_ASN_SUPPRESSION_SHAMAN_AVE(766),
	H_ASN_SUPPRESSION_KAMAEL_AVE(767),
	H_ASN_SUPPRESSION_AVE(768),
	ASSASSIN_CHANGE_ARMOR(769),
	ASN_SHOULDER_AVE(771),
	V_NS_RAID_TOMBSTONE_AVE(772),
	DETHRONE_EVENT_A_AVE(773),
	DETHRONE_EVENT_B_AVE(774),
	H_ASN_AS_MODE_SCREEN(775),
	H_ASN_SHAPE_BLADE_AVE(776),
	H_ASN_SECRET_BOOKA_AVE(777),
	H_ASN_SECRET_BOOKB_AVE(778),
	H_ASN_SECRET_BOOKC_AVE(779),
	H_R_DETHRONE_FIRE_HOLY_AVE(781),
	H_B_DETHRONE_FIRE_BARRIER_AVE(782),
	H_R_SAYHAS_SEER_JUDGMENTA_AVE(783),
	H_R_GHOST_WHISPERA_AVE(784),
	V_RARE_STORM_WALKER_SELF_AVE(785),
	V_RARE_STORM_WALKER_AVE(786),
	H_P_SUMMON_FORCE_OF_ARCANA1A_AVE(787),
	H_B_SUMMON_FORCE_OF_ELEMENTAL1A_AVE(788),
	H_R_SUMMON_PRESTIGE_OF_SPECTRALA_AVE(789),
	V_RARE_FIST_OF_PAAGRIO_BODY_AVE(790),
	V_RARE_FIST_OF_TERA_AVE(791),
	H_W_HIEROPHANT_REDEMPTION_AVE(792),
	H_G_DOOMCRYER_REDEMPTION_AVE(793),
	H_R_DOMINATOR_REDEMPTION_AVE(794),
	H_Y_SWORDMUSE_INSPIRATION_AVE(795),
	H_P_SPECTRALDANCER_INSPIRATION_AVE(796),
	V_RARE_DOOM_BLADE_AVE(797),
	H_G_DETHRONE_FIRE_AVE(798),
	V_RARE_TEMPLAR_ROAR_F_TA(799),
	V_RARE_GUARDIAN_ROAR_F_TA(800),
	V_RARE_BURNING_ROAR_F_TA(801),
	V_RARE_AVENGER_ROAR_F_TA(802),
	H_B_TRIALBUFF01_AVE(803),
	H_B_TRIALBUFF02_AVE(804),
	H_B_TRIALBUFF03_AVE(805),
	H_B_TRIALBUFF04_AVE(806),
	H_R_BERSERKER_SHIELD_BUFF_AVE(808),
	H_R_BERSERKER_SIGIL_DEBUFF_AVE(809),
	H_R_BERSERKER_SIGIL_BUFF_AVE(810),
	H_R_BERSERKER_SHIELD_BUFF_LV7_AVE(811),
	H_R_BERSERKER_SIGIL_BUFF_LV7_AVE(813),
	H_R_BERSERKER_SIGIL_BUFF2_AVE(813),
	H_P_SSGEVENT_WINE_AVE(814),
	AVE_TALISMAN5_BAIUM(815),
	AVE_GRAN_KAIN_PENDENT(816),
	AVE_GRAN_KAIN_PENDENT_5LV(817),
	H_R_CHAMPION_OF_TITANA_AVE(818),
	V_TITAN_INTENSE_PRESSURE1_AVE(819),
	V_TITAN_INTENSE_PRESSURE2_AVE(820),
	V_TITAN_INTENSE_PRESSURE3_AVE(821),
	V_TITAN_TRUE_TOUGHNESS1_AVE(822),
	V_TITAN_TRUE_TOUGHNESS2_AVE(823),
	V_TITAN_TRUE_TOUGHNESS3_AVE(824),
	V_TITAN_DESTROY_ROAR_AVE(825),
	V_EVENT_CANDY01_AVE(827),
	V_EVENT_CANDY02_AVE(828),
	V_EVENT_TEASER_GRADE_AVE(829),
	V_EVENT_TEASER_L_AVE(830),
	V_WIN_SUMMON_SPIRIT_NAVI_AVE(831),
	H_ADENBAG_COIN_TA(832),
	H_R_RUBY_SHIELD_AVE(833),
	JDK_BODY_FIRE(835),
	JDK_BODY_FIRE1(836),
	JDK_BODY_FIRE_BLUE(837),
	JDK_BODY_FIRE_BLUE1(838),
	JDK_BODY_FIRE_PURPLE(839),
	JDK_BODY_FIRE_PURPLE1(840),
	FS_STIGMA_HEAD_AVE(841),
	H_DK_BONEPRISON_RE_A_AVE(842),
	H_DK_ICE_BONEPRISON_RE_A_AVE(843),
	H_DK_LAZER_BONEPRISON_RE_A_AVE(844),
	H_DK_PERFECTSHILED_RE_AVE(846),
	H_DK_ICE_PERFECTSHILED_RE_AVE(847),
	H_DK_DARK_PERFECTSHILED_RE_AVE(848),
	V_DK_FISTOFFURY_HUMAN_AVE(849),
	V_DK_FISTOFFURY_ELF_AVE(850),
	V_DK_FISTOFFURY_DARKELF_AVE(851),
	H_DK_RE_BURNINGFIELD_AVE(852),
	H_DK_RE_FREEZINGAREA_AVE(853),
	H_DK_RE_LIGHTNINGSTORM_AVE(854),
	H_DK_BONEPRISON_RE_B_AVE(856),
	H_DK_ICE_BONEPRISON_RE_B_AVE(857),
	H_DK_LAZER_BONEPRISON_RE_B_AVE(858),
	H_SHINEM_SHIELD_AVE(859),
	H_SHINEM_BUFFA_AVE(860),
	H_SHINEM_DEBUFFA_AVE(861),
	H_SHINEM_DEBUFFB_AVE(862),
	H_SHINEM_WEAKNESS_AVE(863),
	H_G_ARCHER_GUST_AVE(864),
	H_R_ARCHER_FLAME_AVE(865),
	H_R_ARCHER_IGNITION_AVE(866),
	H_R_ARCHER_FLAME_WIND_AVE(867),
	H_B_ARCHER_HUMIDITY_AVE(868),
	H_B_ARCHER_CHILL_AVE(869),
	H_B_ARCHER_SPRAY_AVE(870),
	H_B_ARCHER_WIND_DAMAGE_AVE(871),
	H_B_ARCHER_WIND_AVE(872),
	H_W_ARCHER_STORM_AVE(873),
	H_R_ARCHER_BLAZE_AVE(874),
	H_R_ARCHER_EXPLOSION_AVE(875),
	H_B_ARCHER_COLD_AVE(876),
	H_B_ARCHER_BLIZZARD_AVE(877),
	H_B_ARCHER_WIND_CUTTER_AVE(878),
	H_B_ARCHER_TYPHOON_AVE(879),
	H_B_ARCHER_FOCUS_ARROW_AVE(880),
	H_B_ARCHER_FOCUS_ARROW1_AVE(881),
	H_B_ARCHER_FOCUS_ARROW2_AVE(882),
	H_B_ARCHER_FOCUS_ARROW3_AVE(883),
	H_B_ARCHER_FOCUS_ARROW4_AVE(884),
	H_B_ARCHER_FOCUS_ARROW5_AVE(885),
	V_ARC_MOON_RISING_AVE(886),
	V_ARC_MOON_FULL_AVE(887),
	H_P_BIG_BOW_AVE(888),
	V_ARC_ROCK_ON_AVE(889),
	V_ARC_PIERCING_FLAME_AVE(890),
	V_ARC_PIERCING_AQUA_AVE(891),
	V_ARC_PIERCING_STORM_AVE(892),
	V_GALE_HELMET_AVE(893),
	H_B_DANO_HAIRBUFF_AVE(894),
	S_PERION_NECKLACE_AVE(895),
	S_PERION_TIME_SCREEN(895),
	V_BOSS_PERION_WORSHIP_AVE(897),
	V_BOSS_PERION_CHAIN_AVE(898),
	V_DUAL_CONTENDER_MASTER_AVE(899),
	V_DUAL_BLADE_ASSULT_AVE(901),
	V_DUAL_SPIRIT_BLADE_AVE(902),
	V_DUAL_SONIC_SEAL_AVE(903),
	CHANGE_HALLOWEEN(1000),
	BR_Y_1_ACCESSORY_R_RING(10001),
	BR_Y_1_ACCESSORY_EARRING(10002),
	BR_Y_1_ACCESSORY_NECKRACE(10003),
	BR_Y_2_ACCESSORY_R_RING(10004),
	BR_Y_2_ACCESSORY_EARRING(10005),
	BR_Y_2_ACCESSORY_NECKRACE(10006),
	BR_Y_3_ACCESSORY_R_RING(10007),
	BR_Y_3_ACCESSORY_EARRING(10008),
	BR_Y_3_ACCESSORY_NECKRACE(10009),
	BR_Y_3_TALI_DECO_WING(10019),
	BASEBALL_COSTUME(10020),
	COSTUME_SANTA(10021),
	COSTUME_SANTA_LV2(10022),
	COSTUME_SANTA_LV3(10023),
	COSTUME_SCHOOL(10024),
	COSTUME_SCHOOL_LV2(10025),
	COSTUME_SCHOOL_LV3(10026),
	COSTUME_BEACH(10027),
	COSTUME_BEACH_LV2(10028),
	COSTUME_BEACH_LV3(10029),
	COSTUME_TEDDY(10030),
	COSTUME_TEDDY_LV2(10031),
	COSTUME_TEDDY_LV3(10032),
	COSTUME_KITTY(10033),
	COSTUME_KITTY_LV2(10034),
	COSTUME_KITTY_LV3(10035),
	COSTUME_PANDA(10036),
	COSTUME_PANDA_LV2(10037),
	COSTUME_PANDA_LV3(10038),
	COSTUME_BASEBALL(10039),
	COSTUME_BASEBALL_LV2(10040),
	COSTUME_BASEBALL_LV3(10041),
	COSTUME_SEDUCTIVE(10042),
	COSTUME_SEDUCTIVE_LV2(10043),
	COSTUME_SEDUCTIVE_LV3(10044),
	COSTUME_SCHOOL_B(10045),
	COSTUME_SCHOOL_B_LV2(10046),
	COSTUME_SCHOOL_B_LV3(10047),
	COSTUME_HANBOK(10048),
	COSTUME_HANBOK_LV2(10049),
	COSTUME_HANBOK_LV3(10050),
	COSTUME_METAL(10051),
	COSTUME_METAL_LV2(10052),
	COSTUME_METAL_LV3(10053),
	COSTUME_SAMURAI(10054),
	COSTUME_SAMURAI_LV2(10055),
	COSTUME_SAMURAI_LV3(10056),
	COSTUME_ARCHER_GREEN(10057),
	COSTUME_ARCHER_GREEN_LV2(10058),
	COSTUME_ARCHER_GREEN_LV3(10059),
	COSTUME_FORMAL(10060),
	COSTUME_FORMAL_LV2(10061),
	COSTUME_FORMAL_LV3(10062),
	COSTUME_KAT(10063),
	COSTUME_KAT_LV2(10064),
	COSTUME_KAT_LV3(10065),
	COSTUME_HALLOWEEN(10066),
	COSTUME_HALLOWEEN_LV2(10067),
	COSTUME_HALLOWEEN_LV3(10068),
	COSTUME_ALLURING(10069),
	COSTUME_ALLURING_LV2(10070),
	COSTUME_ALLURING_LV3(10071),
	COSTUME_VAMPIRIC(10072),
	COSTUME_VAMPIRIC_LV2(10073),
	COSTUME_VAMPIRIC_LV3(10074),
	COSTUME_DARK_KNIGHT(10075),
	COSTUME_DARK_KNIGHT_LV2(10076),
	COSTUME_DARK_KNIGHT_LV3(10077),
	COSTUME_CHEVALIER(10078),
	COSTUME_CHEVALIER_LV2(10079),
	COSTUME_CHEVALIER_LV3(10080),
	COSTUME_ARCHER_RED(10081),
	COSTUME_ARCHER_RED_LV2(10082),
	COSTUME_ARCHER_RED_LV3(10083),
	COSTUME_PIRATE_BLUE(10084),
	COSTUME_PIRATE_BLUE_LV2(10085),
	COSTUME_PIRATE_BLUE_LV3(10086),
	COSTUME_WIZARD(10087),
	COSTUME_WIZARD_LV2(10088),
	COSTUME_WIZARD_LV3(10089),
	COSTUME_MAGICIAN(10090),
	COSTUME_MAGICIAN_LV2(10091),
	COSTUME_MAGICIAN_LV3(10092),
	COSTUME_SAILOR(10093),
	COSTUME_SAILOR_LV2(10094),
	COSTUME_SAILOR_LV3(10095),
	COSTUME_NOBLESSE_RED(10096),
	COSTUME_NOBLESSE_RED_LV2(10097),
	COSTUME_NOBLESSE_RED_LV3(10098),
	COSTUME_BARBARIAN(10099),
	COSTUME_BARBARIAN_LV2(10100),
	COSTUME_BARBARIAN_LV3(10101),
	COSTUME_VALKYRIE(10102),
	COSTUME_VALKYRIE_LV2(10103),
	COSTUME_VALKYRIE_LV3(10104),
	COSTUME_KELBIM(10105),
	COSTUME_KELBIM_LV2(10106),
	COSTUME_KELBIM_LV3(10107),
	COSTUME_NINJA(10108),
	COSTUME_NINJA_LV2(10109),
	COSTUME_NINJA_LV3(10110),
	COSTUME_HIGH_PRIEST(10111),
	COSTUME_HIGH_PRIEST_LV2(10112),
	COSTUME_HIGH_PRIEST_LV3(10113),
	COSTUME_NOBLESSE_WHITE(10114),
	COSTUME_NOBLESSE_WHITE_LV2(10115),
	COSTUME_NOBLESSE_WHITE_LV3(10116),
	COSTUME_COWBOY_PURPLE(10117),
	COSTUME_COWBOY_PURPLE_LV2(10118),
	COSTUME_COWBOY_PURPLE_LV3(10119),
	COSTUME_MUSKETEER_BLUE(10120),
	COSTUME_MUSKETEER_BLUE_LV2(10121),
	COSTUME_MUSKETEER_BLUE_LV3(10122),
	COSTUME_ZAKEN(10123),
	COSTUME_ZAKEN_LV2(10124),
	COSTUME_ZAKEN_LV3(10125),
	COSTUME_DRAGON(10126),
	COSTUME_DRAGON_LV2(10127),
	COSTUME_DRAGON_LV3(10128),
	COSTUME_ANAKIM(10129),
	COSTUME_ANAKIM_LV2(10130),
	COSTUME_ANAKIM_LV3(10131),
	COSTUME_LILITH(10132),
	COSTUME_LILITH_LV2(10133),
	COSTUME_LILITH_LV3(10134),
	COSTUME_FREYA(10135),
	COSTUME_FREYA_LV2(10136),
	COSTUME_FREYA_LV3(10137),
	COSTUME_SHADOW_MASTER(10138),
	COSTUME_SHADOW_MASTER_LV2(10139),
	COSTUME_SHADOW_MASTER_LV3(10140),
	COSTUME_NAVY(10141),
	COSTUME_NAVY_LV2(10142),
	COSTUME_NAVY_LV3(10143),
	COSTUME_SKULL(10144),
	COSTUME_SKULL_LV2(10145),
	COSTUME_SKULL_LV3(10146),
	COSTUME_ALICE(10147),
	COSTUME_ALICE_LV2(10148),
	COSTUME_ALICE_LV3(10149),
	COSTUME_DEVIL(10150),
	COSTUME_DEVIL_LV2(10151),
	COSTUME_DEVIL_LV3(10152),
	COSTUME_FERRY_YELLOW(10153),
	COSTUME_FERRY_YELLOW_LV2(10154),
	COSTUME_FERRY_YELLOW_LV3(10155),
	COSTUME_NYNPH(10156),
	COSTUME_NYNPH_LV2(10157),
	COSTUME_NYNPH_LV3(10158),
	COSTUME_MAID(10159),
	COSTUME_MAID_LV2(10160),
	COSTUME_MAID_LV3(10161),
	JDK_BODY_FIRE_2(10162),
	CHANGESHAPE_NINJA_BLACK(10163),
	CHANGESHAPE_COWBOY(10164),
	CHANGESHAPE_MACH(10165),
	CHANGESHAPE_CHINESE(10166),
	CHANGESHAPE_FUT(10167),
	S_TRANS_DECO(10168),
	BR_TRANS_LV2_DECO(10169),
	BR_TRANS_LV3_DECO(10170),
	H_R_PROTECTION_SHIELD_AVE_2(10171),
	Y_INFINITE_SHIELD4_AVE(10172),
	JDK_BODY_FIRE_3(10174);
	
	private final int _clientId;
	
	AbnormalVisualEffect(int clientId)
	{
		_clientId = clientId;
	}
	
	/**
	 * Gets the client id.
	 * @return the client id
	 */
	public int getClientId()
	{
		return _clientId;
	}
	
	/**
	 * Finds abnormal visual effect by name.
	 * @param name the name
	 * @return The abnormal visual effect if its found, {@code null} otherwise
	 */
	public static AbnormalVisualEffect findByName(String name)
	{
		for (AbnormalVisualEffect abnormalVisualEffect : values())
		{
			if (abnormalVisualEffect.name().equalsIgnoreCase(name))
			{
				return abnormalVisualEffect;
			}
		}
		return null;
	}
}