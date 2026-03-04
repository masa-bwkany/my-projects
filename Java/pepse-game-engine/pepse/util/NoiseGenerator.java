package pepse.util;


/**
 * Generates pseudo-random noise using a Perlin-like algorithm.
 * This class is used to introduce natural-looking variations,
 * for example, in terrain height generation. The noise
 * is based on a given seed and a starting point, allowing
 * for consistent and customizable procedural content.
 */
public class NoiseGenerator {
	private static final int PERM_0   = 151;
	private static final int PERM_1   = 160;
	private static final int PERM_2   = 137;
	private static final int PERM_3   = 91;
	private static final int PERM_4   = 90;
	private static final int PERM_5   = 15;
	private static final int PERM_6   = 131;
	private static final int PERM_7   = 13;
	private static final int PERM_8   = 201;
	private static final int PERM_9   = 95;
	private static final int PERM_10  = 96;
	private static final int PERM_11  = 53;
	private static final int PERM_12  = 194;
	private static final int PERM_13  = 233;
	private static final int PERM_14  = 7;
	private static final int PERM_15  = 225;
	private static final int PERM_16  = 140;
	private static final int PERM_17  = 36;
	private static final int PERM_18  = 103;
	private static final int PERM_19  = 30;
	private static final int PERM_20  = 69;
	private static final int PERM_21  = 142;
	private static final int PERM_22  = 8;
	private static final int PERM_23  = 99;
	private static final int PERM_24  = 37;
	private static final int PERM_25  = 240;
	private static final int PERM_26  = 21;
	private static final int PERM_27  = 10;
	private static final int PERM_28  = 23;
	private static final int PERM_29  = 190;
	private static final int PERM_30  = 6;
	private static final int PERM_31  = 148;
	private static final int PERM_32  = 247;
	private static final int PERM_33  = 120;
	private static final int PERM_34  = 234;
	private static final int PERM_35  = 75;
	private static final int PERM_36  = 0;
	private static final int PERM_37  = 26;
	private static final int PERM_38  = 197;
	private static final int PERM_39  = 62;
	private static final int PERM_40  = 94;
	private static final int PERM_41  = 252;
	private static final int PERM_42  = 219;
	private static final int PERM_43  = 203;
	private static final int PERM_44  = 117;
	private static final int PERM_45  = 35;
	private static final int PERM_46  = 11;
	private static final int PERM_47  = 32;
	private static final int PERM_48  = 57;
	private static final int PERM_49  = 177;
	private static final int PERM_50  = 33;
	private static final int PERM_51  = 88;
	private static final int PERM_52  = 237;
	private static final int PERM_53  = 149;
	private static final int PERM_54  = 56;
	private static final int PERM_55  = 87;
	private static final int PERM_56  = 174;
	private static final int PERM_57  = 20;
	private static final int PERM_58  = 125;
	private static final int PERM_59  = 136;
	private static final int PERM_60  = 171;
	private static final int PERM_61  = 168;
	private static final int PERM_62  = 68;
	private static final int PERM_63  = 175;
	private static final int PERM_64  = 74;
	private static final int PERM_65  = 165;
	private static final int PERM_66  = 71;
	private static final int PERM_67  = 134;
	private static final int PERM_68  = 139;
	private static final int PERM_69  = 48;
	private static final int PERM_70  = 27;
	private static final int PERM_71  = 166;
	private static final int PERM_72  = 77;
	private static final int PERM_73  = 146;
	private static final int PERM_74  = 158;
	private static final int PERM_75  = 231;
	private static final int PERM_76  = 83;
	private static final int PERM_77  = 111;
	private static final int PERM_78  = 229;
	private static final int PERM_79  = 122;
	private static final int PERM_80  = 60;
	private static final int PERM_81  = 211;
	private static final int PERM_82  = 133;
	private static final int PERM_83  = 230;
	private static final int PERM_84  = 220;
	private static final int PERM_85  = 105;
	private static final int PERM_86  = 92;
	private static final int PERM_87  = 41;
	private static final int PERM_88  = 55;
	private static final int PERM_89  = 46;
	private static final int PERM_90  = 245;
	private static final int PERM_91  = 40;
	private static final int PERM_92  = 244;
	private static final int PERM_93  = 102;
	private static final int PERM_94  = 143;
	private static final int PERM_95  = 54;
	private static final int PERM_96  = 65;
	private static final int PERM_97  = 25;
	private static final int PERM_98  = 63;
	private static final int PERM_99  = 161;
	private static final int PERM_100 = 1;
	private static final int PERM_101 = 216;
	private static final int PERM_102 = 80;
	private static final int PERM_103 = 73;
	private static final int PERM_104 = 209;
	private static final int PERM_105 = 76;
	private static final int PERM_106 = 132;
	private static final int PERM_107 = 187;
	private static final int PERM_108 = 208;
	private static final int PERM_109 = 89;
	private static final int PERM_110 = 18;
	private static final int PERM_111 = 169;
	private static final int PERM_112 = 200;
	private static final int PERM_113 = 196;
	private static final int PERM_114 = 135;
	private static final int PERM_115 = 130;
	private static final int PERM_116 = 116;
	private static final int PERM_117 = 188;
	private static final int PERM_118 = 159;
	private static final int PERM_119 = 86;
	private static final int PERM_120 = 164;
	private static final int PERM_121 = 100;
	private static final int PERM_122 = 109;
	private static final int PERM_123 = 198;
	private static final int PERM_124 = 173;
	private static final int PERM_125 = 186;
	private static final int PERM_126 = 3;
	private static final int PERM_127 = 64;
	private static final int PERM_128 = 52;
	private static final int PERM_129 = 217;
	private static final int PERM_130 = 226;
	private static final int PERM_131 = 250;
	private static final int PERM_132 = 124;
	private static final int PERM_133 = 123;
	private static final int PERM_134 = 5;
	private static final int PERM_135 = 202;
	private static final int PERM_136 = 38;
	private static final int PERM_137 = 147;
	private static final int PERM_138 = 118;
	private static final int PERM_139 = 126;
	private static final int PERM_140 = 255;
	private static final int PERM_141 = 82;
	private static final int PERM_142 = 85;
	private static final int PERM_143 = 212;
	private static final int PERM_144 = 207;
	private static final int PERM_145 = 206;
	private static final int PERM_146 = 59;
	private static final int PERM_147 = 227;
	private static final int PERM_148 = 47;
	private static final int PERM_149 = 16;
	private static final int PERM_150 = 58;
	private static final int PERM_151 = 17;
	private static final int PERM_152 = 182;
	private static final int PERM_153 = 189;
	private static final int PERM_154 = 28;
	private static final int PERM_155 = 42;
	private static final int PERM_156 = 223;
	private static final int PERM_157 = 183;
	private static final int PERM_158 = 170;
	private static final int PERM_159 = 213;
	private static final int PERM_160 = 119;
	private static final int PERM_161 = 248;
	private static final int PERM_162 = 152;
	private static final int PERM_163 = 2;
	private static final int PERM_164 = 44;
	private static final int PERM_165 = 154;
	private static final int PERM_166 = 163;
	private static final int PERM_167 = 70;
	private static final int PERM_168 = 221;
	private static final int PERM_169 = 153;
	private static final int PERM_170 = 101;
	private static final int PERM_171 = 155;
	private static final int PERM_172 = 167;
	private static final int PERM_173 = 43;
	private static final int PERM_174 = 172;
	private static final int PERM_175 = 9;
	private static final int PERM_176 = 129;
	private static final int PERM_177 = 22;
	private static final int PERM_178 = 39;
	private static final int PERM_179 = 253;
	private static final int PERM_180 = 19;
	private static final int PERM_181 = 98;
	private static final int PERM_182 = 108;
	private static final int PERM_183 = 110;
	private static final int PERM_184 = 79;
	private static final int PERM_185 = 113;
	private static final int PERM_186 = 224;
	private static final int PERM_187 = 232;
	private static final int PERM_188 = 178;
	private static final int PERM_189 = 185;
	private static final int PERM_190 = 112;
	private static final int PERM_191 = 104;
	private static final int PERM_192 = 218;
	private static final int PERM_193 = 246;
	private static final int PERM_194 = 97;
	private static final int PERM_195 = 228;
	private static final int PERM_196 = 251;
	private static final int PERM_197 = 34;
	private static final int PERM_198 = 242;
	private static final int PERM_199 = 193;
	private static final int PERM_200 = 238;
	private static final int PERM_201 = 210;
	private static final int PERM_202 = 144;
	private static final int PERM_203 = 12;
	private static final int PERM_204 = 191;
	private static final int PERM_205 = 179;
	private static final int PERM_206 = 162;
	private static final int PERM_207 = 241;
	private static final int PERM_208 = 81;
	private static final int PERM_209 = 51;
	private static final int PERM_210 = 145;
	private static final int PERM_211 = 235;
	private static final int PERM_212 = 249;
	private static final int PERM_213 = 14;
	private static final int PERM_214 = 239;
	private static final int PERM_215 = 107;
	private static final int PERM_216 = 49;
	private static final int PERM_217 = 192;
	private static final int PERM_218 = 214;
	private static final int PERM_219 = 31;
	private static final int PERM_220 = 181;
	private static final int PERM_221 = 199;
	private static final int PERM_222 = 106;
	private static final int PERM_223 = 157;
	private static final int PERM_224 = 184;
	private static final int PERM_225 = 84;
	private static final int PERM_226 = 204;
	private static final int PERM_227 = 176;
	private static final int PERM_228 = 115;
	private static final int PERM_229 = 121;
	private static final int PERM_230 = 50;
	private static final int PERM_231 = 45;
	private static final int PERM_232 = 127;
	private static final int PERM_233 = 4;
	private static final int PERM_234 = 150;
	private static final int PERM_235 = 254;
	private static final int PERM_236 = 138;
	private static final int PERM_237 = 236;
	private static final int PERM_238 = 205;
	private static final int PERM_239 = 93;
	private static final int PERM_240 = 222;
	private static final int PERM_241 = 114;
	private static final int PERM_242 = 67;
	private static final int PERM_243 = 29;
	private static final int PERM_244 = 24;
	private static final int PERM_245 = 72;
	private static final int PERM_246 = 243;
	private static final int PERM_247 = 141;
	private static final int PERM_248 = 128;
	private static final int PERM_249 = 195;
	private static final int PERM_250 = 78;
	private static final int PERM_251 = 66;
	private static final int PERM_252 = 215;
	private static final int PERM_253 = 61;
	private static final int PERM_254 = 156;
	private static final int PERM_255 = 180;
	private static final long INITIAL_DEFAULT_SIZE      = 35;
	private static final int PERMUTATION_LENGTH         = 256;
	private static final int PERMUTATION_ARRAY_SIZE     =  2;
	private static final double SIZE_REDUCTION_FACTOR   = 2.0;
	private static final int FADE_COEFF_A               = 6;
	private static final int FADE_COEFF_B               = 15;
	private static final int FADE_COEFF_C               = 10;
	private static final int  GRAD_THRESHOLD_U         = 8;
	private static final int  GRAD_THRESHOLD_V         = 4;
	private static final int  GRAD_ALT_1               = 12;
	private static final int  GRAD_ALT_2               = 14;
	private static final int  HASH_MASK               = 255;
	private static final int PERMUTATION_ARRAY_SIZE2   = 512;

	// 0xFF, used for &-masking

	private double seed;
	private long default_size;
	private int[] p;
	private int[] permutation;
	private double startPoint;

	/**
	 * The constructor of the NoiseGenerator class.
	 *
	 * @param seed       can be anything you want (even 1234 or new Random().nextGaussian()).
	 *                   This seed is the basis of the random generator, which
	 *                   will draw upon it to generate pseudo-random noise.
	 * @param startPoint is a relative point that the noise will be generated from.
	 *                   In our case it should be your ground height at X0 (specified in
	 *                   ex4 when we talk about the terrain: 2.2.1).
	 */
	public NoiseGenerator(double seed, int startPoint) {
		this.seed = seed;
		this.startPoint = startPoint;
		init();
	}
	/**
	 * Initializes the permutation array (p) based on a predefined permutation table.
	 * This array is crucial for the Perlin noise algorithm, providing the
	 * pseudo-randomness. It populates the array by duplicating the initial
	 * permutation to allow for indexing with wrapping.
	 */
	private void init() {
		// Initialize the permutation array.
		this.p = new int[PERMUTATION_ARRAY_SIZE2 ];
		this.permutation = new int[]{PERM_0, PERM_1, PERM_2, PERM_3, PERM_4, PERM_5, PERM_6, PERM_7,
				PERM_8, PERM_9, PERM_10, PERM_11, PERM_12, PERM_13, PERM_14, PERM_15,
				PERM_16, PERM_17, PERM_18, PERM_19, PERM_20, PERM_21, PERM_22, PERM_23,
				PERM_24, PERM_25, PERM_26, PERM_27, PERM_28, PERM_29, PERM_30, PERM_31,
				PERM_32, PERM_33, PERM_34, PERM_35, PERM_36, PERM_37, PERM_38, PERM_39,
				PERM_40, PERM_41, PERM_42, PERM_43, PERM_44, PERM_45, PERM_46, PERM_47,
				PERM_48, PERM_49, PERM_50, PERM_51, PERM_52, PERM_53, PERM_54, PERM_55,
				PERM_56, PERM_57, PERM_58, PERM_59, PERM_60, PERM_61, PERM_62, PERM_63,
				PERM_64, PERM_65, PERM_66, PERM_67, PERM_68, PERM_69, PERM_70, PERM_71,
				PERM_72, PERM_73, PERM_74, PERM_75, PERM_76, PERM_77, PERM_78, PERM_79,
				PERM_80, PERM_81, PERM_82, PERM_83, PERM_84, PERM_85, PERM_86, PERM_87,
				PERM_88, PERM_89, PERM_90, PERM_91, PERM_92, PERM_93, PERM_94, PERM_95,
				PERM_96, PERM_97, PERM_98, PERM_99, PERM_100, PERM_101, PERM_102, PERM_103,
				PERM_104, PERM_105, PERM_106, PERM_107, PERM_108, PERM_109, PERM_110, PERM_111,
				PERM_112, PERM_113, PERM_114, PERM_115, PERM_116, PERM_117, PERM_118, PERM_119,
				PERM_120, PERM_121, PERM_122, PERM_123, PERM_124, PERM_125, PERM_126, PERM_127,
				PERM_128, PERM_129, PERM_130, PERM_131, PERM_132, PERM_133, PERM_134, PERM_135,
				PERM_136, PERM_137, PERM_138, PERM_139, PERM_140, PERM_141, PERM_142, PERM_143,
				PERM_144, PERM_145, PERM_146, PERM_147, PERM_148, PERM_149, PERM_150, PERM_151,
				PERM_152, PERM_153, PERM_154, PERM_155, PERM_156, PERM_157, PERM_158, PERM_159,
				PERM_160, PERM_161, PERM_162, PERM_163, PERM_164, PERM_165, PERM_166, PERM_167,
				PERM_168, PERM_169, PERM_170, PERM_171, PERM_172, PERM_173, PERM_174, PERM_175,
				PERM_176, PERM_177, PERM_178, PERM_179, PERM_180, PERM_181, PERM_182, PERM_183,
				PERM_184, PERM_185, PERM_186, PERM_187, PERM_188, PERM_189, PERM_190, PERM_191,
				PERM_192, PERM_193, PERM_194, PERM_195, PERM_196, PERM_197, PERM_198, PERM_199,
				PERM_200, PERM_201, PERM_202, PERM_203, PERM_204, PERM_205, PERM_206, PERM_207,
				PERM_208, PERM_209, PERM_210, PERM_211, PERM_212, PERM_213, PERM_214, PERM_215,
				PERM_216, PERM_217, PERM_218, PERM_219, PERM_220, PERM_221, PERM_222, PERM_223,
				PERM_224, PERM_225, PERM_226, PERM_227, PERM_228, PERM_229, PERM_230, PERM_231,
				PERM_232, PERM_233, PERM_234, PERM_235, PERM_236, PERM_237, PERM_238, PERM_239,
				PERM_240, PERM_241, PERM_242, PERM_243, PERM_244, PERM_245, PERM_246, PERM_247,
				PERM_248, PERM_249, PERM_250, PERM_251, PERM_252, PERM_253, PERM_254, PERM_255
		};
		this.default_size = INITIAL_DEFAULT_SIZE ;
		// Populate it
		for (int i = 0; i < PERMUTATION_LENGTH ; i++) {
			p[PERMUTATION_LENGTH  + i] = p[i] = permutation[i];
		}
	}

	/**
	 * Noise is responsible to generate pseudo random noise according to the
	 * seed given upon constructing the object.
	 *
	 * @param x      the wanted x to receive noise for (in our case, the x
	 *                  coordinate of the terrain you'd want to create).
	 * @param factor describes how large the noise should be (play with it,
	 *                 but BLOCK_SIZE *7 should be enough).
	 * @return returns a noise you should *add* to the groundHeightAtX0 you have.
	 * <p>
	 * example:
	 * public float groundHeightAt(float x) {
	 * float noise = (float) noiseGenerator.noise(x, BLOCK_SIZE *7);
	 * return groundHeightAtX0 + noise;
	 * }
	 */
	public double noise(double x, double factor) {
		double value = 0.0;
		double currentPoint = startPoint;

		while (currentPoint >= 1) {
			value += smoothNoise((x / currentPoint), 0, 0) * currentPoint;
			currentPoint /= SIZE_REDUCTION_FACTOR;
		}

		return value * factor / startPoint;
	}


	/**
	 * Computes the smooth noise value for a given 3D point using the Perlin noise algorithm.
	 * This method finds the unit cube containing the point, calculates the relative coordinates,
	 * applies fade curves, hashes the coordinates of the 8 cube corners, and then blends
	 * the results using linear interpolation.
	 *
	 * @param x The x-coordinate of the point.
	 * @param y The y-coordinate of the point.
	 * @param z The z-coordinate of the point.
	 * @return The smooth noise value for the given point.
	 */
	private double smoothNoise(double x, double y, double z) {
		// Offset each coordinate by the seed value
		x += this.seed;
		y += this.seed;
		x += this.seed;

		int X = (int) Math.floor(x) & HASH_MASK; // FIND UNIT CUBE THAT
		int Y = (int) Math.floor(y) & HASH_MASK; // CONTAINS POINT.
		int Z = (int) Math.floor(z) & HASH_MASK;

		x -= Math.floor(x); // FIND RELATIVE X,Y,Z
		y -= Math.floor(y); // OF POINT IN CUBE.
		z -= Math.floor(z);

		double u = fade(x); // COMPUTE FADE CURVES
		double v = fade(y); // FOR EACH OF X,Y,Z.
		double w = fade(z);

		int A = p[X] + Y;
		int AA = p[A] + Z;
		int AB = p[A + 1] + Z; // HASH COORDINATES OF
		int B = p[X + 1] + Y;
		int BA = p[B] + Z;
		int BB = p[B + 1] + Z; // THE 8 CUBE CORNERS,

		return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z),    // AND ADD
								grad(p[BA], x - 1, y, z)), // BLENDED
						lerp(u, grad(p[AB], x, y - 1, z),    // RESULTS
								grad(p[BB], x - 1, y - 1, z))),// FROM 8
				lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1),    // CORNERS
								grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
						lerp(u, grad(p[AB + 1], x, y - 1, z - 1),
								grad(p[BB + 1], x - 1, y - 1, z - 1))));
	}

	/**
	 * Computes the fade curve (also known as Ken Perlin's smootherstep function)
	 * for a given value t. This function ensures that the derivative is zero at
	 * t=0 and t=1, which helps to avoid discontinuities and create smoother transitions
	 * in the noise.
	 *
	 * @param t The input value, typically between 0.0 and 1.0.
	 * @return The faded value.
	 */
	private double fade(double t) {
		return t * t * t * (t * (t * FADE_COEFF_A  - FADE_COEFF_B) + FADE_COEFF_C  );
	}

	/**
	 * Performs linear interpolation (lerp) between two values.
	 *
	 * @param t The interpolation factor, typically between 0.0 and 1.0.
	 * @param a The start value.
	 * @param b The end value.
	 * @return The interpolated value.
	 */
	private double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}

	/**
	 * Calculates the gradient value for a given hash and a 3D point.
	 * This method maps the lower 4 bits of the hash code to one of 12 gradient directions,
	 * then computes the dot product of the gradient vector with the input point's
	 * relative coordinates.
	 *
	 * @param hash The hashed value for a corner of the unit cube.
	 * @param x    The x-coordinate relative to the cube corner.
	 * @param y    The y-coordinate relative to the cube corner.
	 * @param z    The z-coordinate relative to the cube corner.
	 * @return The gradient value.
	 */
	private double grad(int hash, double x, double y, double z) {
		int h = hash & FADE_COEFF_B; // CONVERT LO 4 BITS OF HASH CODE
		double u = h < GRAD_THRESHOLD_U  ? x : y, // INTO 12 GRADIENT DIRECTIONS.
				v = h < GRAD_THRESHOLD_V ? y : h == GRAD_ALT_1 || h == GRAD_ALT_2 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & PERMUTATION_ARRAY_SIZE) == 0 ? v : -v);
	}
}
