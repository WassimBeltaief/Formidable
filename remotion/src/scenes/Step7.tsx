import React from "react";
import { interpolate } from "remotion";

export const STEP7_START = 740;
export const STEP7_END   = 890; // 5 s

const fadeUp = (frame: number, start: number, duration = 14) =>
  interpolate(frame, [start, start + duration], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

const slideUp = (frame: number, start: number, duration = 14) =>
  interpolate(frame, [start, start + duration], [12, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

const PURPLE = "#6750A4";

export const Step7: React.FC<{ frame: number; opacity?: number }> = ({ frame, opacity = 1 }) => {
  const local = Math.max(0, frame - STEP7_START);

  // Staggered reveal: logo → version → tagline → pills
  const logoOpacity   = fadeUp(local, 0);
  const logoY         = slideUp(local, 0);
  const verOpacity    = fadeUp(local, 16);
  const verY          = slideUp(local, 16);
  const tagOpacity    = fadeUp(local, 30);
  const tagY          = slideUp(local, 30);
  const pillsOpacity  = fadeUp(local, 46);

  // Subtle glow pulse on the version badge
  const glowPulse = interpolate(
    Math.sin((local - 46) * 0.07),
    [-1, 1],
    [0.15, 0.4],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );

  return (
    <div style={{
      position: "absolute", inset: 0,
      display: "flex", flexDirection: "column",
      alignItems: "center", justifyContent: "center",
      opacity,
      gap: 0,
    }}>

      {/* Logo wordmark */}
      <div style={{
        opacity: logoOpacity,
        transform: `translateY(${logoY}px)`,
        fontSize: 72,
        fontWeight: "900",
        fontFamily: "sans-serif",
        color: "#111",
        letterSpacing: -2,
        lineHeight: 1,
        marginBottom: 16,
      }}>
        Formidable
      </div>

      {/* Version badge */}
      <div style={{
        opacity: verOpacity,
        transform: `translateY(${verY}px)`,
        marginBottom: 28,
      }}>
        <div style={{
          display: "inline-block",
          background: PURPLE,
          borderRadius: 100,
          padding: "6px 20px",
          fontSize: 18,
          fontFamily: "monospace",
          fontWeight: "700",
          color: "#fff",
          letterSpacing: 1,
          boxShadow: `0 0 32px rgba(103,80,164,${glowPulse})`,
        }}>
          v2.0.0
        </div>
      </div>

      {/* Tagline */}
      <div style={{
        opacity: tagOpacity,
        transform: `translateY(${tagY}px)`,
        fontSize: 16,
        fontFamily: "sans-serif",
        color: "#666",
        letterSpacing: 0.3,
        marginBottom: 32,
      }}>
        Headless · Schema-driven · KSP-powered
      </div>

      {/* Platform pills */}
      <div style={{
        opacity: pillsOpacity,
        display: "flex",
        gap: 10,
      }}>
        {["Android", "iOS", "Web"].map((p) => (
          <div key={p} style={{
            padding: "5px 16px",
            borderRadius: 100,
            border: "1px solid rgba(103,80,164,0.3)",
            fontSize: 12,
            fontFamily: "sans-serif",
            color: PURPLE,
            fontWeight: "600",
            background: "rgba(103,80,164,0.06)",
          }}>
            {p}
          </div>
        ))}
      </div>

    </div>
  );
};
