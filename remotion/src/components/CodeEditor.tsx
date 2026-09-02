import React from "react";

export const CodeEditor: React.FC<{
  filename: string;
  fontSize?: number;
  children: React.ReactNode;
}> = ({ filename, fontSize = 18, children }) => (
  <div style={{
    background: "#FFFFFF",
    borderRadius: 14,
    border: "1px solid rgba(0,0,0,0.08)",
    boxShadow: "0 4px 24px rgba(0,0,0,0.08)",
    overflow: "hidden",
  }}>
    {/* Window chrome */}
    <div style={{
      display: "flex",
      alignItems: "center",
      gap: 7,
      padding: "14px 20px",
      borderBottom: "1px solid rgba(0,0,0,0.06)",
      background: "#F8F8F8",
    }}>
      <div style={{ width: 10, height: 10, borderRadius: "50%", background: "#FF5F57" }} />
      <div style={{ width: 10, height: 10, borderRadius: "50%", background: "#FFBD2E" }} />
      <div style={{ width: 10, height: 10, borderRadius: "50%", background: "#28C840" }} />
      <span style={{
        marginLeft: 10,
        color: "rgba(0,0,0,0.35)",
        fontSize: 13,
        fontFamily: "monospace",
      }}>
        {filename}
      </span>
    </div>

    {/* Code area */}
    <div style={{
      padding: "22px 28px 26px",
      fontFamily: '"JetBrains Mono", "Fira Code", monospace',
      fontSize,
      lineHeight: 1.65,
      color: "#1D1D1D",
    }}>
      {children}
    </div>
  </div>
);
