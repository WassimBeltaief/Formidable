import React from "react";
import { interpolate, spring, useVideoConfig } from "remotion";
import { CodeEditor } from "../components/CodeEditor";
import { CodeLine } from "../components/CodeLine";

// Step 3:
// 1. Zoom in on "data class LoginForm("
// 2. @FormSchema fades in above it
// 3. Phone slides in from the right the moment @FormSchema appears

export const STEP3_START = 60;
export const STEP3_END   = 125;

const ANNOTATION_START  = STEP3_START + 8;  // 68  — @FormSchema fades in
const ANNOTATION_END    = STEP3_START + 20; // 80  — fully visible

// 3 blinks: each blink = 8 frames (4 lit, 4 off)
const BLINK_START       = STEP3_START + 22; // 82
const BLINK_INTERVAL    = 8;
const BLINK_COUNT       = 1;
const BLINK_END         = BLINK_START + BLINK_COUNT * BLINK_INTERVAL; // 106

const PHONE_SLIDE_START = BLINK_END + 2;    // 108  — phone only after blinking done

const FORM_LINES = [
  "data class LoginForm(",
  '    val email: String = "",',
  '    val password: String = "",',
  "    val contactMethod: ContactMethod = ContactMethod.EMAIL,",
  "    val rememberMe: Boolean = false,",
  ")",
];

// ── Phone skeleton ────────────────────────────────────────────────────

const SkeletonBar: React.FC<{ width?: string; height?: number; delay: number; parentOpacity: number }> = ({
  width = "100%", height = 46, delay, parentOpacity,
}) => (
  <div style={{
    width,
    height,
    borderRadius: 4,
    background: "rgba(255,255,255,0.08)",
    border: "1.5px solid rgba(255,255,255,0.1)",
    marginBottom: 12,
    opacity: parentOpacity,
    transform: `translateY(${(1 - parentOpacity) * 10}px)`,
    transition: "none",
  }} />
);

const PhoneSkeleton: React.FC<{ opacity: number; translateX: number }> = ({
  opacity, translateX,
}) => {
  return (
    <div style={{
      width: 300,
      borderRadius: 34,
      border: "8px solid #2C2C3E",
      background: "#1C1B1F",
      boxShadow: "0 20px 50px rgba(0,0,0,0.3)",
      overflow: "hidden",
      opacity,
      transform: `translateX(${translateX}px)`,
    }}>
      {/* Status bar */}
      <div style={{
        height: 22, background: "#1C1B1F",
        display: "flex", alignItems: "center",
        justifyContent: "flex-end", paddingRight: 14,
      }}>
        <span style={{ color: "#E6E1E5", fontSize: 9, opacity: 0.4, fontFamily: "sans-serif" }}>9:41</span>
      </div>

      <div style={{ padding: "12px 18px 20px" }}>
        {/* Title */}
        <div style={{ marginBottom: 18 }}>
          <div style={{ color: "#E6E1E5", fontSize: 16, fontWeight: "bold", fontFamily: "sans-serif" }}>
            Login
          </div>
          <div style={{ color: "#938F99", fontSize: 10, fontFamily: "sans-serif", marginTop: 2 }}>
            Powered by{" "}
            <span style={{ color: "#BB86FC", fontWeight: "600" }}>Formidable</span>
          </div>
        </div>

        {/* Skeleton fields — 4 grey bars */}
        {[0, 1, 2, 3].map((i) => (
          <SkeletonBar key={i} delay={i * 3} height={44} parentOpacity={opacity} />
        ))}

        {/* Skeleton button */}
        <div style={{
          height: 40,
          borderRadius: 100,
          background: "rgba(103,80,164,0.35)",
          marginTop: 4,
          opacity,
        }} />
      </div>
    </div>
  );
};

// ── Step 3 ────────────────────────────────────────────────────────────

export const Step3: React.FC<{ frame: number; opacity?: number }> = ({ frame, opacity = 1 }) => {
  const { fps } = useVideoConfig();

  // @FormSchema fade in
  const annotationOpacity = interpolate(
    frame,
    [ANNOTATION_START, ANNOTATION_END],
    [0, 1],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );
  const annotationY = interpolate(
    frame,
    [ANNOTATION_START, ANNOTATION_END],
    [-8, 0],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );

  // @FormSchema blink highlight — 3 alternating pulses
  const isBlinking = frame >= BLINK_START && frame < BLINK_END;
  const blinkLit = isBlinking
    ? Math.floor((frame - BLINK_START) / (BLINK_INTERVAL / 2)) % 2 === 0
    : false;

  // Phone slides in only after blinking finishes
  const phoneSpring  = spring({ fps, frame: frame - PHONE_SLIDE_START, config: { damping: 18, mass: 0.9 } });
  const phoneX       = interpolate(Math.min(phoneSpring, 1), [0, 1], [60, 0]);
  const phoneOpacity = Math.min(phoneSpring, 1);

  return (
    <div style={{
      position: "absolute", inset: 0,
      display: "flex", alignItems: "center", justifyContent: "center",
      opacity,
    }}>
      <div style={{ display: "flex", alignItems: "center", gap: 40 }}>

        {/* Code panel */}
        <div style={{ width: 800, flexShrink: 0 }}>
          <CodeEditor filename="LoginForm.kt" fontSize={14}>
            {/* @FormSchema — fades in, then blinks */}
            <div style={{
              opacity: annotationOpacity,
              transform: `translateY(${annotationY}px)`,
            }}>
              <div style={{
                display: "inline-flex",
                background: blinkLit ? "rgba(103,80,164,0.22)" : "transparent",
                borderRadius: 5,
                padding: "1px 6px",
                marginLeft: -6,
                boxShadow: blinkLit ? "0 0 14px rgba(103,80,164,0.45)" : "none",
              }}>
                <CodeLine text="@FormSchema" />
              </div>
            </div>
            {FORM_LINES.map((line, i) => (
              <CodeLine key={i} text={line} />
            ))}
          </CodeEditor>
        </div>

        {/* Phone skeleton */}
        <PhoneSkeleton opacity={phoneOpacity} translateX={phoneX} />
      </div>
    </div>
  );
};
