import React from "react";
import { interpolate, spring, useVideoConfig } from "remotion";
import {
  BLOCK_TIMING,
  DEMO_CONTACT_TOGGLE,
  DEMO_EMAIL_VISIBLE,
  EMAIL_APPEARS_FRAME,
  EMAIL_HIDE_FRAME,
} from "./stages";

const PURPLE = "#6750A4";
const SURFACE = "#1C1B1F";
const FIELD_BG = "#2B2930";
const TEXT = "#E6E1E5";
const SUBTEXT = "#CAC4D0";
const OUTLINE = "#938F99";
const ERROR_COLOR = "#F2B8B5";

type FieldProps = {
  label: string;
  hint?: string;
  value?: string;
  error?: string;
  password?: boolean;
  selector?: boolean;
  highlight?: boolean;
  spring: number;
};

const FormField: React.FC<FieldProps> = ({
  label, hint, value, error, password, selector, highlight, spring: s,
}) => {
  const clampedS = Math.min(s, 1);
  const opacity = clampedS;
  const translateY = interpolate(clampedS, [0, 1], [18, 0]);

  const displayValue = password && value ? "•".repeat(value.length) : value;
  const borderColor = highlight ? PURPLE : error ? ERROR_COLOR : OUTLINE;

  return (
    <div style={{ opacity, transform: `translateY(${translateY}px)`, marginBottom: 14 }}>
      <div
        style={{
          border: `1.5px solid ${borderColor}`,
          borderRadius: 4,
          padding: "12px 14px 8px",
          position: "relative",
          background: FIELD_BG,
          boxShadow: highlight ? `0 0 0 3px rgba(103,80,164,0.25)` : "none",
          transition: "box-shadow 0.3s, border-color 0.3s",
        }}
      >
        <div
          style={{
            position: "absolute",
            top: -9,
            left: 10,
            background: SURFACE,
            padding: "0 4px",
            fontSize: 11,
            color: error ? ERROR_COLOR : highlight ? PURPLE : SUBTEXT,
            fontFamily: "sans-serif",
            fontWeight: "500",
          }}
        >
          {label}
        </div>

        {selector ? (
          <div style={{
            color: TEXT, fontSize: 13, fontFamily: "sans-serif",
            display: "flex", justifyContent: "space-between", alignItems: "center",
          }}>
            <span>{value}</span>
            <span style={{ color: SUBTEXT, fontSize: 10 }}>▾</span>
          </div>
        ) : (
          <div style={{ color: value ? TEXT : SUBTEXT, fontSize: 13, fontFamily: "sans-serif", minHeight: 18 }}>
            {displayValue ?? hint}
          </div>
        )}
      </div>

      {error && (
        <div style={{
          color: ERROR_COLOR, fontSize: 11, fontFamily: "sans-serif",
          marginTop: 3, paddingLeft: 14,
        }}>
          {error}
        </div>
      )}
    </div>
  );
};

const Toggle: React.FC<{ label: string; spring: number }> = ({ label, spring: s }) => {
  const clampedS = Math.min(s, 1);
  return (
    <div style={{
      opacity: clampedS,
      transform: `translateY(${interpolate(clampedS, [0, 1], [18, 0])}px)`,
      display: "flex", alignItems: "center", gap: 10, marginBottom: 14,
    }}>
      <div style={{
        width: 36, height: 20, borderRadius: 10, background: PURPLE, position: "relative", flexShrink: 0,
      }}>
        <div style={{
          position: "absolute", right: 2, top: 2,
          width: 16, height: 16, borderRadius: "50%", background: "#E8DEF8",
        }} />
      </div>
      <span style={{ color: TEXT, fontFamily: "sans-serif", fontSize: 13 }}>{label}</span>
    </div>
  );
};

function useFieldSpring(triggerFrame: number, frame: number, fps: number) {
  return spring({ fps, frame: frame - triggerFrame, config: { damping: 18, mass: 0.8 } });
}

export const PhoneMockup: React.FC<{ frame: number }> = ({ frame }) => {
  const { fps } = useVideoConfig();

  const firstNameS  = useFieldSpring(BLOCK_TIMING[1][0], frame, fps);
  const passwordS   = useFieldSpring(BLOCK_TIMING[2][0], frame, fps);
  const contactS    = useFieldSpring(BLOCK_TIMING[3][0], frame, fps);
  const rememberMeS = useFieldSpring(BLOCK_TIMING[5][0], frame, fps);
  const titleS      = useFieldSpring(BLOCK_TIMING[0][0], frame, fps);

  // Email: appears at EMAIL_APPEARS_FRAME, hides at EMAIL_HIDE_FRAME, re-appears at DEMO_EMAIL_VISIBLE
  const emailAppearS = useFieldSpring(EMAIL_APPEARS_FRAME, frame, fps);
  const emailHideS   = useFieldSpring(EMAIL_HIDE_FRAME,    frame, fps);
  const emailShowS   = useFieldSpring(DEMO_EMAIL_VISIBLE,  frame, fps);

  let emailOpacity: number;
  let emailTranslateY: number;
  if (frame < EMAIL_HIDE_FRAME) {
    const s = Math.min(emailAppearS, 1);
    emailOpacity    = s;
    emailTranslateY = interpolate(s, [0, 1], [18, 0]);
  } else if (frame < DEMO_EMAIL_VISIBLE) {
    const s = Math.min(emailHideS, 1);
    emailOpacity    = interpolate(s, [0, 1], [1, 0]);
    emailTranslateY = interpolate(s, [0, 1], [0, -14]);
  } else {
    const s = Math.min(emailShowS, 1);
    emailOpacity    = s;
    emailTranslateY = interpolate(s, [0, 1], [18, 0]);
  }

  // contactMethod value + highlight pulse during demo
  const contactMethodValue = frame >= DEMO_CONTACT_TOGGLE ? "EMAIL" : "PHONE";
  const contactHighlightS  = useFieldSpring(DEMO_CONTACT_TOGGLE, frame, fps);
  const contactIsHighlighted =
    frame >= DEMO_CONTACT_TOGGLE && frame < DEMO_CONTACT_TOGGLE + 45;
  const emailGlow =
    frame >= DEMO_EMAIL_VISIBLE && frame < DEMO_EMAIL_VISIBLE + 50;

  // Annotation label under @VisibleWhen demo
  const visibleWhenLabelOpacity = interpolate(
    frame,
    [EMAIL_HIDE_FRAME, EMAIL_HIDE_FRAME + 20],
    [0, 1],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );

  return (
    <div style={{
      width: 290,
      borderRadius: 36,
      border: "8px solid #2C2C3E",
      background: SURFACE,
      boxShadow: "0 24px 60px rgba(0,0,0,0.35)",
      overflow: "hidden",
      display: "flex",
      flexDirection: "column",
    }}>
      {/* Status bar */}
      <div style={{
        height: 24,
        background: SURFACE,
        display: "flex",
        alignItems: "center",
        justifyContent: "flex-end",
        paddingRight: 14,
        flexShrink: 0,
      }}>
        <span style={{ color: TEXT, fontSize: 10, opacity: 0.5, fontFamily: "sans-serif" }}>9:41</span>
      </div>

      {/* Content */}
      <div style={{ flex: 1, padding: "14px 20px 20px", overflowY: "hidden" }}>
        {/* Title */}
        <div style={{
          opacity: Math.min(titleS, 1),
          transform: `translateY(${interpolate(Math.min(titleS, 1), [0, 1], [-10, 0])}px)`,
          marginBottom: 18,
        }}>
          <div style={{ color: TEXT, fontSize: 18, fontWeight: "bold", fontFamily: "sans-serif" }}>
            Sign Up
          </div>
          <div style={{ color: SUBTEXT, fontSize: 11, fontFamily: "sans-serif", marginTop: 2 }}>
            Powered by{" "}
            <span style={{ color: "#BB86FC", fontWeight: "600" }}>Formidable</span>
          </div>
        </div>

        <FormField
          label="First Name"
          hint="Your first name"
          value="Alice"
          spring={firstNameS}
        />

        <FormField
          label="Password"
          hint="At least 8 characters"
          value="mysecret1"
          password
          spring={passwordS}
        />

        <FormField
          label="Contact method"
          value={contactMethodValue}
          selector
          highlight={contactIsHighlighted}
          spring={contactS}
        />

        {/* @VisibleWhen badge */}
        {frame >= EMAIL_HIDE_FRAME && frame < DEMO_EMAIL_VISIBLE && (
          <div style={{
            opacity: visibleWhenLabelOpacity,
            display: "flex",
            alignItems: "center",
            gap: 6,
            marginBottom: 10,
          }}>
            <div style={{
              background: "rgba(103,80,164,0.15)",
              border: "1px solid rgba(103,80,164,0.4)",
              borderRadius: 100,
              padding: "3px 10px",
              color: "#BB86FC",
              fontSize: 10,
              fontFamily: "monospace",
            }}>
              @VisibleWhen contactMethod = "EMAIL"
            </div>
          </div>
        )}

        {/* Email — animated in/out */}
        <div style={{
          opacity: emailOpacity,
          transform: `translateY(${emailTranslateY}px)`,
          marginBottom: 14,
        }}>
          <div style={{
            border: `1.5px solid ${emailGlow ? PURPLE : OUTLINE}`,
            borderRadius: 4,
            padding: "12px 14px 8px",
            position: "relative",
            background: FIELD_BG,
            boxShadow: emailGlow ? `0 0 0 3px rgba(103,80,164,0.3)` : "none",
            transition: "box-shadow 0.3s, border-color 0.3s",
          }}>
            <div style={{
              position: "absolute", top: -9, left: 10,
              background: SURFACE, padding: "0 4px",
              fontSize: 11, color: emailGlow ? PURPLE : SUBTEXT,
              fontFamily: "sans-serif", fontWeight: "500",
            }}>
              Email{" "}
              <span style={{ color: SUBTEXT, fontSize: 10, fontWeight: "400" }}>(optional)</span>
            </div>
            <div style={{ color: SUBTEXT, fontSize: 13, fontFamily: "sans-serif", minHeight: 18 }}>
              Your email address
            </div>
          </div>
        </div>

        <Toggle label="Remember me" spring={rememberMeS} />

        {/* Submit button */}
        {frame >= BLOCK_TIMING[5][0] && (
          <div style={{
            opacity: Math.min(
              spring({ fps, frame: frame - BLOCK_TIMING[5][0] - 10, config: { damping: 18 } }),
              1
            ),
            background: PURPLE,
            borderRadius: 100,
            padding: "11px 0",
            textAlign: "center",
            color: "#fff",
            fontWeight: "bold",
            fontFamily: "sans-serif",
            fontSize: 13,
            letterSpacing: 0.3,
          }}>
            Create account
          </div>
        )}
      </div>
    </div>
  );
};
