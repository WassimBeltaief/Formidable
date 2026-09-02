import React from "react";
import { useCurrentFrame, interpolate } from "remotion";

const TOKEN_COLORS: Record<string, string> = {
  annotation: "#6F42C1",
  keyword: "#0033B3",
  type: "#267F99",
  string: "#067D17",
  param: "#1D1D1D",
  punctuation: "#555555",
  comment: "#9E9E9E",
};

type Token = { text: string; color: string };

function tokenize(line: string): Token[] {
  const tokens: Token[] = [];
  let rest = line;

  const rules: [RegExp, string][] = [
    [/^(@\w+)/, "annotation"],
    [/^(data class|class|val|fun|override|suspend|return)(?=\b)/, "keyword"],
    [/^(String|Boolean|Int|Unit|Map|Any)(?=\b)/, "type"],
    [/^"[^"]*"/, "string"],
    [/^\/\/.*/, "comment"],
    [/^[(){}:=,<>?*[\].]/, "punctuation"],
    [/^\s+/, "param"],
    [/^\w+/, "param"],
  ];

  while (rest.length > 0) {
    let matched = false;
    for (const [regex, color] of rules) {
      const m = rest.match(regex);
      if (m) {
        tokens.push({ text: m[0], color: TOKEN_COLORS[color] });
        rest = rest.slice(m[0].length);
        matched = true;
        break;
      }
    }
    if (!matched) {
      tokens.push({ text: rest[0], color: TOKEN_COLORS.param });
      rest = rest.slice(1);
    }
  }
  return tokens;
}

const CODE_LINES = [
  "@FormSchema",
  "data class SignUpForm(",
  "",
  '    @Field(label = "First Name", hint = "Your first name")',
  '    @NotBlank(message = "First name is required")',
  "    val firstName: String = \"\",",
  "",
  '    @Field(label = "Password", hint = "At least 8 characters")',
  '    @NotBlank(order = 1, message = "Password is required")',
  '    @MinLength(order = 2, min = 8, message = "Password must be at least 8 characters")',
  "    val password: String = \"\",",
  "",
  '    @Field(label = "Email", hint = "Your email address", optional = true)',
  '    @VisibleWhen(targetField = "contactMethod", targetValue = "EMAIL")',
  '    @RequiredIf(order = 1, targetField = "contactMethod", targetValue = "EMAIL", message = "Email is required")',
  '    @Email(order = 2, message = "Please enter a valid email")',
  "    val email: String? = null,",
  "",
  '    @Field(label = "Remember me")',
  "    val rememberMe: Boolean = false,",
  ")",
];

const CHARS_PER_FRAME = 4;

export const CodeAnimation: React.FC<{ progress: number }> = ({ progress }) => {
  const fullText = CODE_LINES.join("\n");
  const totalChars = fullText.length;
  const visibleChars = Math.floor(progress * totalChars);

  let charCount = 0;
  const renderedLines = CODE_LINES.map((line, lineIdx) => {
    const lineStart = charCount;
    charCount += line.length + 1; // +1 for newline

    if (charCount <= visibleChars) {
      // Full line visible
      return (
        <div key={lineIdx} style={{ display: "flex", flexWrap: "wrap" }}>
          {tokenize(line).map((tok, i) => (
            <span key={i} style={{ color: tok.color }}>
              {tok.text}
            </span>
          ))}
        </div>
      );
    } else if (lineStart < visibleChars) {
      // Partial line
      const visible = visibleChars - lineStart;
      const partialLine = line.slice(0, visible);
      return (
        <div key={lineIdx} style={{ display: "flex", flexWrap: "wrap" }}>
          {tokenize(partialLine).map((tok, i) => (
            <span key={i} style={{ color: tok.color }}>
              {tok.text}
            </span>
          ))}
          <span
            style={{
              display: "inline-block",
              width: "2px",
              height: "1.2em",
              background: "#555555",
              animation: "none",
              marginLeft: "1px",
              verticalAlign: "text-bottom",
            }}
          />
        </div>
      );
    }
    return <div key={lineIdx} />;
  });

  return (
    <div
      style={{
        fontFamily: '"JetBrains Mono", "Fira Code", monospace',
        fontSize: 17,
        lineHeight: 1.65,
        color: "#1D1D1D",
      }}
    >
      {renderedLines}
    </div>
  );
};
