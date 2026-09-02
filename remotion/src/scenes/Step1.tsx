import React from "react";
import { interpolate } from "remotion";
import { CodeEditor } from "../components/CodeEditor";

// Step 1: start typing the bare class — two lines, then hold.
// This establishes "this is just a normal Kotlin data class"

const LINES = [
  "data class LoginForm(",
  '    val email: String = "",',
];

// Timing (frames at 30fps)
const EDITOR_FADE_IN_END = 5;
const TYPE_LINE1_START = 5;
const TYPE_LINE1_END   = 18;
const TYPE_LINE2_START = 21;
const TYPE_LINE2_END   = 37;
export const STEP1_END = 45;

export const Step1: React.FC<{ frame: number }> = ({ frame }) => {
  const editorOpacity = interpolate(frame, [0, EDITOR_FADE_IN_END], [0, 1], {
    extrapolateRight: "clamp",
  });
  const editorY = interpolate(frame, [0, EDITOR_FADE_IN_END], [14, 0], {
    extrapolateRight: "clamp",
  });

  // Build the visible text char by char
  const line1Chars = Math.floor(
    interpolate(frame, [TYPE_LINE1_START, TYPE_LINE1_END], [0, LINES[0].length], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
    })
  );
  const line2Chars = Math.floor(
    interpolate(frame, [TYPE_LINE2_START, TYPE_LINE2_END], [0, LINES[1].length], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
    })
  );

  const visibleLines: { text: string; done: boolean }[] = [];

  if (line1Chars > 0) {
    visibleLines.push({
      text: LINES[0].slice(0, line1Chars),
      done: line1Chars >= LINES[0].length,
    });
  }
  if (frame >= TYPE_LINE2_START && line2Chars >= 0) {
    visibleLines.push({
      text: LINES[1].slice(0, line2Chars),
      done: line2Chars >= LINES[1].length,
    });
  }

  // Cursor blinks using sin wave — only visible on the last incomplete line
  const cursorVisible = Math.sin(frame * 0.25) > 0;
  const lastLineIdx = visibleLines.length - 1;

  return (
    <AbsoluteFillCentered>
      <div
        style={{
          opacity: editorOpacity,
          transform: `translateY(${editorY}px)`,
          width: 680,
        }}
      >
        <CodeEditor filename="LoginForm.kt">
          {visibleLines.map((line, i) => (
            <CodeLine
              key={i}
              text={line.text}
              showCursor={i === lastLineIdx && cursorVisible}
            />
          ))}
        </CodeEditor>
      </div>
    </AbsoluteFillCentered>
  );
};

// ── small layout helpers ──────────────────────────────────────────────

const AbsoluteFillCentered: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <div style={{
    position: "absolute", inset: 0,
    display: "flex", alignItems: "center", justifyContent: "center",
  }}>
    {children}
  </div>
);

const TOKEN_COLORS: Record<string, string> = {
  annotation:  "#6F42C1",
  keyword:     "#0033B3",
  type:        "#267F99",
  string:      "#067D17",
  plain:       "#1D1D1D",
  punctuation: "#555555",
};

type Token = { text: string; color: string };

function tokenize(line: string): Token[] {
  const rules: [RegExp, string][] = [
    [/^(@\w+)/,                                               "annotation"],
    [/^(data class|class|val|fun|override|suspend|return)\b/, "keyword"],
    [/^(String|Boolean|Int|Unit|Map|Any)\b/,                  "type"],
    [/^"[^"]*"/,                                              "string"],
    [/^[(){}:=,<>?*[\].]/,                                    "punctuation"],
    [/^\s+/,                                                  "plain"],
    [/^\w+/,                                                  "plain"],
  ];
  const tokens: Token[] = [];
  let rest = line;
  while (rest.length > 0) {
    let matched = false;
    for (const [re, color] of rules) {
      const m = rest.match(re);
      if (m) {
        tokens.push({ text: m[0], color: TOKEN_COLORS[color] });
        rest = rest.slice(m[0].length);
        matched = true;
        break;
      }
    }
    if (!matched) {
      tokens.push({ text: rest[0], color: TOKEN_COLORS.plain });
      rest = rest.slice(1);
    }
  }
  return tokens;
}

const CodeLine: React.FC<{ text: string; showCursor: boolean }> = ({ text, showCursor }) => (
  <div style={{ display: "flex", alignItems: "baseline", minHeight: "1.6em" }}>
    {tokenize(text).map((tok, i) => (
      <span key={i} style={{ color: tok.color, whiteSpace: "pre" }}>{tok.text}</span>
    ))}
    {showCursor && (
      <span style={{
        display: "inline-block",
        width: 2,
        height: "1em",
        background: "#444",
        verticalAlign: "text-bottom",
        marginLeft: 1,
      }} />
    )}
  </div>
);
