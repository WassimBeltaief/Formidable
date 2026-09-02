import React from "react";
import { interpolate } from "remotion";
import { CodeEditor } from "../components/CodeEditor";
import { CodeLine } from "../components/CodeLine";

export const STEP6_START = 320;

// 7 features × 60 frames = 420 frames = 14 s
const FRAMES_PER_FEATURE = 60;
const FEATURE_COUNT = 7;
export const STEP6_END = STEP6_START + FEATURE_COUNT * FRAMES_PER_FEATURE; // 740

// ── Feature data ──────────────────────────────────────────────────────

const FEATURES: Array<{
  category: string;
  headline: string;
  sub?: string;
  code: string[];
  badge: string;
}> = [
  {
    category: "STATE MANAGEMENT",
    headline: "Zero\nboilerplate.",
    code: [
      "// StateFlow<FieldState<String>>",
      "//   per field — no ViewModel wiring",
    ],
    badge: "Compile-time · KSP",
  },
  {
    category: "VALIDATION",
    headline: "Annotate.\nValidate.",
    code: [
      "    EmailValidator",
      "    IntRangeValidator",
      "    MatchFieldValidator",
      "    MaxLengthValidator",
      "    MinLengthValidator",
      "    MustBeTrueValidator",
      "    ...",
    ],
    badge: "8 built-in validators",
  },
  {
    category: "ASYNC VALIDATION",
    headline: "Await.\nDebounce.\nDone.",
    code: [
      "    @AsyncValidation(",
      "        UniqueUsernameValidator::class",
      "    )",
    ],
    badge: "Coroutines · debounce built-in",
  },
  {
    category: "CROSS-FIELD RULES",
    headline: "Fields that\ntalk to each\nother.",
    code: [
      '    @MatchField(targetField = "password")',
      '    @VisibleWhen(targetField = "contactMethod",',
      '                 targetValue = "EMAIL")',
    ],
    badge: "@MatchField  @RequiredIf  @VisibleWhen",
  },
  {
    category: "FOCUS & KEYBOARD",
    headline: "Tab.\nDone.\nJust work.",
    code: [
      "    @FocusOrder(1)",
      "    @ImeAction(ImeAction.Next)",
      '    val email: String = "",',
      "",
      "    @FocusOrder(2)",
      "    @ImeAction(ImeAction.Done)",
      '    val password: String = "",',
    ],
    badge: "No FocusRequester wiring needed",
  },
  {
    category: "RENDERING",
    headline: "Headless\nor Material 3.\nYour call.",
    code: [
      "StringField() {",
      "    // any Composable — TextField, chip, slider…",
      "}",
    ],
    badge: "formidable-material3 optional",
  },
  {
    category: "MULTIPLATFORM",
    headline: "Android.\niOS.\nWeb.",
    code: [
      "// formidable-core — pure Kotlin",
      "// No Android dependency",
      "",
      "// Android · iOS · WASM ✅",
    ],
    badge: "Compose Multiplatform",
  },
];

// ── Sub-components ────────────────────────────────────────────────────

const PURPLE = "#6750A4";
const PURPLE_LIGHT = "rgba(103,80,164,0.1)";

const CategoryChip: React.FC<{ text: string }> = ({ text }) => (
  <div style={{
    display: "inline-block",
    background: PURPLE_LIGHT,
    border: "1px solid rgba(103,80,164,0.25)",
    borderRadius: 100,
    padding: "4px 12px",
    fontSize: 11,
    fontFamily: "sans-serif",
    fontWeight: "700",
    color: PURPLE,
    letterSpacing: 1.2,
    marginBottom: 18,
  }}>
    {text}
  </div>
);

const Headline: React.FC<{ text: string }> = ({ text }) => (
  <div style={{
    fontSize: 36,
    fontWeight: "800",
    fontFamily: "sans-serif",
    color: "#111",
    lineHeight: 1.15,
    marginBottom: 12,
    whiteSpace: "pre-line",
  }}>
    {text}
  </div>
);

const Sub: React.FC<{ text: string }> = ({ text }) => (
  <div style={{
    fontSize: 13,
    fontFamily: "sans-serif",
    color: "#666",
    lineHeight: 1.5,
    marginBottom: 18,
    maxWidth: 260,
  }}>
    {text}
  </div>
);

const Badge: React.FC<{ text: string }> = ({ text }) => (
  <div style={{
    marginTop: 16,
    display: "inline-flex",
    alignItems: "center",
    gap: 6,
    background: "#F0FDF4",
    border: "1px solid #BBF7D0",
    borderRadius: 100,
    padding: "5px 12px",
    fontSize: 11,
    fontFamily: "sans-serif",
    color: "#166534",
    fontWeight: "600",
  }}>
    <span style={{ fontSize: 13 }}>✅</span> {text}
  </div>
);

// ── Step 6 ────────────────────────────────────────────────────────────

export const Step6: React.FC<{ frame: number; opacity?: number }> = ({ frame, opacity = 1 }) => {
  const localFrame = Math.max(0, frame - STEP6_START);
  const featureIndex = Math.min(
    Math.floor(localFrame / FRAMES_PER_FEATURE),
    FEATURE_COUNT - 1
  );
  const featureLocalFrame = localFrame - featureIndex * FRAMES_PER_FEATURE;

  const feature = FEATURES[featureIndex];

  const cardOpacity = interpolate(
    featureLocalFrame,
    [0, 8, FRAMES_PER_FEATURE - 8, FRAMES_PER_FEATURE],
    [0, 1, 1, 0],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );
  const cardY = interpolate(
    featureLocalFrame,
    [0, 12],
    [16, 0],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );

  return (
    <div style={{
      position: "absolute", inset: 0,
      display: "flex", alignItems: "center", justifyContent: "center",
      opacity,
    }}>
      <div style={{
        display: "flex",
        alignItems: "flex-start",
        gap: 40,
        opacity: cardOpacity,
        transform: `translateY(${cardY}px)`,
      }}>

        {/* Left — text */}
        <div style={{ width: 340, flexShrink: 0 }}>
          <CategoryChip text={feature.category} />
          <Headline text={feature.headline} />
          {feature.sub && <Sub text={feature.sub} />}
          <Badge text={feature.badge} />
        </div>

        {/* Right — code */}
        <div style={{ width: 760, flexShrink: 0 }}>
          <CodeEditor filename="example.kt" fontSize={13}>
            {feature.code.map((line, i) => (
              <CodeLine key={i} text={line} />
            ))}
          </CodeEditor>
        </div>

      </div>
    </div>
  );
};
