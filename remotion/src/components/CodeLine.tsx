import React from "react";
import { tokenize } from "../utils/tokenize";

export const CodeLine: React.FC<{
  text: string;
  opacity?: number;
  translateY?: number;
  showCursor?: boolean;
}> = ({ text, opacity = 1, translateY = 0, showCursor = false }) => (
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
