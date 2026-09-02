import React from "react";
import { interpolate } from "remotion";
import { CodeEditor } from "../components/CodeEditor";
import { CodeLine } from "../components/CodeLine";

export const STEP5_START = 220;
export const STEP5_END   = 320;

const SCENE_FADE_END    = STEP5_START + 8;  // 228 — scene fades in
const VAL_FADE_START    = STEP5_START + 6;  // 226
const VAL_FADE_END      = STEP5_START + 18; // 238
const ANNO_FADE_START   = STEP5_START + 22; // 242
const ANNO_FADE_END     = STEP5_START + 34; // 254
const BLINK_START       = STEP5_START + 36; // 256
const BLINK_INTERVAL    = 8;
const BLINK_END         = BLINK_START + BLINK_INTERVAL; // 264

const LABEL_FADE_START  = BLINK_END;
const LABEL_FADE_END    = BLINK_END + 14;
const ERROR_FADE_START  = BLINK_END + 18;
const ERROR_FADE_END    = BLINK_END + 30;

const ANNOTATIONS = [
  '    @Field(label = "Password", hint = "At least 8 characters")',
  '    @NotBlank(message = "Password is required")',
  '    @MinLength(order = 2, min = 8, message = "Password must be at least 8 characters")',
];

// ── Phone ─────────────────────────────────────────────────────────────

const SURFACE  = "#1C1B1F";
const FIELD_BG = "#2B2930";
const PURPLE   = "#BB86FC";
const OUTLINE  = "#938F99";
const SUBTEXT  = "#CAC4D0";
const TEXT     = "#E6E1E5";
const ERROR_COL = "#F2B8B5";

const PhonePasswordField: React.FC<{ frame: number }> = ({ frame }) => {
  const labelOpacity = interpolate(frame, [LABEL_FADE_START, LABEL_FADE_END], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const errorOpacity = interpolate(frame, [ERROR_FADE_START, ERROR_FADE_END], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const showError = frame >= ERROR_FADE_START;

  return (
    <div style={{
      width: 300,
      borderRadius: 34,
      border: "8px solid #2C2C3E",
      background: SURFACE,
      boxShadow: "0 20px 50px rgba(0,0,0,0.3)",
      overflow: "hidden",
    }}>
        <div style={{
          height: 22, background: SURFACE,
          display: "flex", alignItems: "center",
          justifyContent: "flex-end", paddingRight: 14,
        }}>
          <span style={{ color: TEXT, fontSize: 9, opacity: 0.4, fontFamily: "sans-serif" }}>9:41</span>
        </div>

        <div style={{ padding: "12px 18px 20px" }}>
          <div style={{ marginBottom: 16 }}>
            <div style={{ color: TEXT, fontSize: 15, fontWeight: "bold", fontFamily: "sans-serif" }}>Login</div>
            <div style={{ color: SUBTEXT, fontSize: 10, fontFamily: "sans-serif", marginTop: 2 }}>
              Powered by <span style={{ color: PURPLE, fontWeight: "600" }}>Formidable</span>
            </div>
          </div>

          {/* Email field — resolved, small */}
          <div style={{
            height: 44, borderRadius: 4,
            background: FIELD_BG,
            border: `1.5px solid ${OUTLINE}`,
            marginBottom: 12,
            display: "flex", alignItems: "center",
            paddingLeft: 14, position: "relative",
          }}>
            <div style={{
              position: "absolute", top: -9, left: 10,
              background: SURFACE, padding: "0 4px",
              fontSize: 11, color: PURPLE, fontFamily: "sans-serif",
            }}>Email</div>
            <span style={{ color: SUBTEXT, fontSize: 12, fontFamily: "sans-serif" }}>Your email address</span>
          </div>

          {/* Password field — active */}
          <div style={{ marginBottom: 12 }}>
            <div style={{
              border: `1.5px solid ${showError ? ERROR_COL : OUTLINE}`,
              borderRadius: 4,
              padding: "12px 14px 8px",
              position: "relative",
              background: FIELD_BG,
            }}>
              <div style={{
                position: "absolute", top: -9, left: 10,
                background: SURFACE, padding: "0 4px",
                fontSize: 11, color: showError ? ERROR_COL : PURPLE,
                fontFamily: "sans-serif", fontWeight: "500",
                opacity: labelOpacity,
              }}>
                Password
              </div>
              <div style={{ color: SUBTEXT, fontSize: 12, fontFamily: "sans-serif", minHeight: 18, opacity: labelOpacity }}>
                At least 8 characters
              </div>
            </div>
            {showError && (
              <div style={{
                color: ERROR_COL, fontSize: 11, fontFamily: "sans-serif",
                marginTop: 3, paddingLeft: 14, opacity: errorOpacity,
              }}>
                Password is required
              </div>
            )}
          </div>

          {/* Remaining fields as skeleton */}
          {[0, 1].map((i) => (
            <div key={i} style={{
              height: 44, borderRadius: 4,
              background: "rgba(255,255,255,0.06)",
              border: "1.5px solid rgba(255,255,255,0.08)",
              marginBottom: 12,
            }} />
          ))}
          <div style={{ height: 40, borderRadius: 100, background: "rgba(103,80,164,0.35)", marginTop: 4 }} />
        </div>
      </div>
  );
};

// ── Step 5 ────────────────────────────────────────────────────────────

export const Step5: React.FC<{ frame: number; opacity?: number }> = ({ frame, opacity = 1 }) => {
  const valOpacity = interpolate(frame, [VAL_FADE_START, VAL_FADE_END], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const valY = interpolate(frame, [VAL_FADE_START, VAL_FADE_END], [6, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const annoOpacity = interpolate(frame, [ANNO_FADE_START, ANNO_FADE_END], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const annoY = interpolate(frame, [ANNO_FADE_START, ANNO_FADE_END], [-8, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const isBlinking = frame >= BLINK_START && frame < BLINK_END;
  const blinkLit = isBlinking
    ? Math.floor((frame - BLINK_START) / (BLINK_INTERVAL / 2)) % 2 === 0
    : false;

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
            {/* Annotations */}
            <div style={{ opacity: annoOpacity, transform: `translateY(${annoY}px)` }}>
              <div style={{
                background: blinkLit ? "rgba(103,80,164,0.15)" : "transparent",
                borderRadius: 5,
                padding: "2px 6px",
                marginLeft: -6,
                boxShadow: blinkLit ? "0 0 14px rgba(103,80,164,0.35)" : "none",
              }}>
                {ANNOTATIONS.map((line, i) => (
                  <CodeLine key={i} text={line} />
                ))}
              </div>
            </div>

            {/* val password */}
            <div style={{ opacity: valOpacity, transform: `translateY(${valY}px)` }}>
              <CodeLine text='    val password: String = "",' />
            </div>
          </CodeEditor>
        </div>

        {/* Phone */}
        <PhonePasswordField frame={frame} />
      </div>
    </div>
  );
};
