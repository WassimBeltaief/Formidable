// Code blocks — each entry is one field group (annotations + val)
export const CODE_BLOCKS: string[][] = [
  // 0: class header
  ["@FormSchema", "data class SignUpForm("],

  // 1: firstName
  [
    '    @Field(label = "First Name", hint = "Your first name")',
    '    @NotBlank(message = "First name is required")',
    '    val firstName: String = "",',
    "",
  ],

  // 2: password
  [
    '    @Field(label = "Password", hint = "At least 8 characters")',
    '    @NotBlank(order = 1, message = "Password is required")',
    '    @MinLength(order = 2, min = 8, message = "Password must be at least 8 characters")',
    '    val password: String = "",',
    "",
  ],

  // 3: contactMethod
  [
    '    @Field(label = "Contact method")',
    '    val contactMethod: String = "PHONE",',
    "",
  ],

  // 4: email — contains @VisibleWhen
  [
    '    @Field(label = "Email", hint = "Your email address", optional = true)',
    '    @VisibleWhen(targetField = "contactMethod", targetValue = "EMAIL")',
    '    @RequiredIf(order = 1, targetField = "contactMethod",',
    '        targetValue = "EMAIL", message = "Email is required")',
    '    @Email(order = 2, message = "Please enter a valid email")',
    '    val email: String? = null,',
    "",
  ],

  // 5: rememberMe
  [
    '    @Field(label = "Remember me")',
    "    val rememberMe: Boolean = false,",
  ],

  // 6: closing brace
  [")"],
];

// [typeStart, typeEnd] frames for each block
export const BLOCK_TIMING: [number, number][] = [
  [5,   28 ],  // 0: header       (~33 chars)
  [38,  108],  // 1: firstName    (~137 chars)
  [118, 228],  // 2: password     (~235 chars)
  [238, 268],  // 3: contactMethod (~76 chars)
  [278, 418],  // 4: email        (~323 chars)
  [428, 462],  // 5: rememberMe   (~68 chars)
  [468, 478],  // 6: closing
];

// Frame when @VisibleWhen is fully typed (block 4, ~44% through)
// Lines 1+2 of block 4: 73+69=142 chars out of ~323 total
// 278 + (142/323)*140 ≈ 278 + 62 = 340
export const EMAIL_APPEARS_FRAME = 278;
export const EMAIL_HIDE_FRAME = 345;

// Demo phase: toggle contactMethod → email re-appears
export const DEMO_CONTACT_TOGGLE = 510;
export const DEMO_EMAIL_VISIBLE = 558;

export const FINAL_START = 620;
export const TOTAL_FRAMES = 690;
