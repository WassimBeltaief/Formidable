import React from "react";
import { interpolate } from "remotion";
import { CODE_BLOCKS, BLOCK_TIMING } from "./stages";

const TOKEN_COLORS: Record<string, string> = {
  annotation: "#6F42C1",
  keyword:    "#0033B3",
  type:       "#267F99",
  string:     "#067D17",
  param:      "#1D1D1D",
  punctuation:"#555555",
  comment:    "#9E9E9E",
};

type Token = { text: string; color: string };

function tokenize(line: string): Token[] {
  const rules: [RegExp, string][] = [
    [/^(@\w+)/,                                               "annotation"],
    [/^(data class|class|val|fun|override|suspend|return)\b/, "keyword"],
    [/^(String|Boolean|Int|Unit|Map|Any)\b/,                  "type"],
    [/^"[^"]*"/,                                              "string"],
    [/^\/\/.*/,                                               "comment"],
    [/^[(){}:=,<>?*[\].]/,                                    "punctuation"],
    [/^\s+/,                                                  "param"],
    [/^\w+/,                                                  "param"],
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
      tokens.push({ text: rest[0], color: TOKEN_COLORS.param });
      rest = rest.slice(1);
    }
  }
  return tokens;
}

function renderLine(line: string, key: number) {
  return (
    <div key={key} style={{ display: "flex", flexWrap: "nowrap", minHeight: "1.5em" }}>
      {tokenize(line).map((tok, i) => (
        <span key={i} style={{ color: tok.color, whiteSpace: "pre" }}>
          {tok.text}
        </span>
      ))}
    </div>
  );
}

function renderPartialLine(line: string, visibleChars: number, key: number) {
  const partial = line.slice(0, visibleChars);
  return (
    <div key={key} style={{ display: "flex", flexWrap: "nowrap", minHeight: "1.5em" }}>
      {tokenize(partial).map((tok, i) => (
        <span key={i} style={{ color: tok.color, whiteSpace: "pre" }}>
          {tok.text}
        </span>
      ))}
      <span
        style={{
          display: "inline-block",
          width: 2,
          height: "1em",
          background: "#444",
          verticalAlign: "text-bottom",
          marginLeft: 1,
        }}
      />
    </div>
  );
}

const FONT_SIZE = 14;
const LINE_HEIGHT = 1.5;
const LINE_HEIGHT_PX = FONT_SIZE * LINE_HEIGHT;
const MAX_VISIBLE_LINES = 17;

export const CodePanel: React.FC<{ frame: number }> = ({ frame }) => {
  // Compute which lines are visible and how many chars of the current block are typed
  let totalLinesVisible = 0;
  const blockNodes: React.ReactNode[] = [];

  for (let b = 0; b < CODE_BLOCKS.length; b++) {
    const block = CODE_BLOCKS[b];
    const [typeStart, typeEnd] = BLOCK_TIMING[b];

    if (frame < typeStart) break;

    const blockText = block.join("\n");
    const totalChars = blockText.length;

    let visibleChars: number;
    if (frame >= typeEnd) {
      visibleChars = totalChars;
    } else {
      const p = interpolate(frame, [typeStart, typeEnd], [0, 1], { extrapolateRight: "clamp" });
      visibleChars = Math.floor(p * totalChars);
    }

    // Render lines
    const lineNodes: React.ReactNode[] = [];
    let charCount = 0;

    for (let l = 0; l < block.length; l++) {
      const line = block[l];
      const lineStart = charCount;
      charCount += line.length + 1; // +1 for the implicit \n between lines

      if (charCount <= visibleChars) {
        lineNodes.push(renderLine(line, l));
        totalLinesVisible++;
      } else if (lineStart < visibleChars) {
        const partial = visibleChars - lineStart;
        lineNodes.push(renderPartialLine(line, partial, l));
        totalLinesVisible++;
        break;
      } else {
        break;
      }
    }

    const isCurrentBlock = frame >= typeStart && frame < typeEnd;
    blockNodes.push(
      <div
        key={b}
        style={{
          borderLeft: isCurrentBlock ? "2px solid rgba(103,80,164,0.5)" : "2px solid transparent",
          paddingLeft: 8,
          background: isCurrentBlock ? "rgba(103,80,164,0.04)" : "transparent",
        }}
      >
        {lineNodes}
      </div>
    );
  }

  const scrollY = Math.max(0, (totalLinesVisible - MAX_VISIBLE_LINES) * LINE_HEIGHT_PX);

  return (
    <div style={{ overflow: "hidden", height: MAX_VISIBLE_LINES * LINE_HEIGHT_PX }}>
      <div
        style={{
          transform: `translateY(-${scrollY}px)`,
          transition: "transform 0.2s ease",
          fontFamily: '"JetBrains Mono", "Fira Code", monospace',
          fontSize: FONT_SIZE,
          lineHeight: LINE_HEIGHT,
          color: "#1D1D1D",
        }}
      >
        {blockNodes}
      </div>
    </div>
  );
};
