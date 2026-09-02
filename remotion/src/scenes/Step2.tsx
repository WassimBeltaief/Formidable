import React from "react";
import { interpolate } from "remotion";
import { CodeEditor } from "../components/CodeEditor";

// Step 2: remaining fields fade in all at once — "this is just a data class"

const LINE1 = "data class LoginForm(";
const LINE2 = '    val email: String = "",';
const NEW_LINES = [
  '    val password: String = "",',
  "    val contactMethod: ContactMethod = ContactMethod.EMAIL,",
  "    val rememberMe: Boolean = false,",
  ")",
];

const FADE_DURATION = 15; // 0.5s

export const STEP2_START = 45;
export const STEP2_END   = STEP2_START + FADE_DURATION; // 60

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
    [/^(String|Boolean|Int|Unit|Map|Any|ContactMethod)\b/,    "type"],
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

const CodeLine: React.FC<{
  text: string;
  opacity?: number;
  translateY?: number;
}> = ({ text, opacity = 1, translateY = 0 }) => (
  <div style={{
    display: "flex",
    alignItems: "baseline",
    minHeight: "1.6em",
    opacity,
    transform: `translateY(${translateY}px)`,
  }}>
    {tokenize(text).map((tok, i) => (
      <span key={i} style={{ color: tok.color, whiteSpace: "pre" }}>{tok.text}</span>
    ))}
  </div>
);

export const Step2: React.FC<{ frame: number }> = ({ frame }) => {
  const localFrame = frame - STEP2_START;

  const fadeOpacity = interpolate(localFrame, [0, FADE_DURATION], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const fadeY = interpolate(localFrame, [0, FADE_DURATION], [6, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  return (
    <div style={{
      position: "absolute", inset: 0,
      display: "flex", alignItems: "center", justifyContent: "center",
    }}>
      <div style={{ width: 680 }}>
        <CodeEditor filename="LoginForm.kt">
          <CodeLine text={LINE1} />
          <CodeLine text={LINE2} />
          {NEW_LINES.map((line, i) => (
            <CodeLine key={i} text={line} opacity={fadeOpacity} translateY={fadeY} />
          ))}
        </CodeEditor>
      </div>
    </div>
  );
};
