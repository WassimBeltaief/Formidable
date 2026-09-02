import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame } from "remotion";
import { Step1 } from "./scenes/Step1";
import { Step2, STEP2_START } from "./scenes/Step2";
import { Step3, STEP3_START, STEP3_END } from "./scenes/Step3";
import { Step4, STEP4_START, STEP4_END } from "./scenes/Step4";
import { Step5, STEP5_START, STEP5_END } from "./scenes/Step5";
import { Step6, STEP6_START, STEP6_END } from "./scenes/Step6";
import { Step7, STEP7_START } from "./scenes/Step7";

const BG = "#FAFAFA";
const FADE = 10; // cross-fade frames

function fadeOut(frame: number, end: number) {
  return interpolate(frame, [end - FADE, end], [1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
}
function fadeIn(frame: number, start: number) {
  return interpolate(frame, [start, start + FADE], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
}

export const FormidableIntro: React.FC = () => {
  const frame = useCurrentFrame();

  const step3Opacity = Math.min(fadeIn(frame, STEP3_START), fadeOut(frame, STEP3_END));
  const step4Opacity = Math.min(fadeIn(frame, STEP4_START), fadeOut(frame, STEP4_END));
  const step5Opacity = Math.min(fadeIn(frame, STEP5_START), fadeOut(frame, STEP5_END));
  const step6Opacity = Math.min(fadeIn(frame, STEP6_START), fadeOut(frame, STEP6_END));
  const step7Opacity = fadeIn(frame, STEP7_START);

  return (
    <AbsoluteFill style={{ background: BG }}>
      {/* Subtle grid */}
      <AbsoluteFill
  style={{
    backgroundImage: `
      linear-gradient(rgba(0,0,0,0.045) 1px, transparent 1px),
      linear-gradient(90deg, rgba(0,0,0,0.045) 1px, transparent 1px)
    `,
    backgroundSize: "48px 48px",
  }}
  from={-61} />

      {/* Steps 1 & 2 — centered, no phone yet */}
      {frame < STEP3_START && (
        <>
          {frame < STEP2_START && <Step1 frame={frame} />}
          {frame >= STEP2_START && <Step2 frame={frame} />}
        </>
      )}

      {/* Steps 3-5 — cross-faded, fixed 620/260px two-column layout */}
      {frame >= STEP3_START - FADE && frame < STEP4_START + FADE && (
        <Step3 frame={frame} opacity={step3Opacity} />
      )}
      {frame >= STEP4_START - FADE && frame < STEP5_START + FADE && (
        <Step4 frame={frame} opacity={step4Opacity} />
      )}
      {frame >= STEP5_START - FADE && frame < STEP6_START + FADE && (
        <Step5 frame={frame} opacity={step5Opacity} />
      )}
      {frame >= STEP6_START - FADE && frame < STEP7_START + FADE && (
        <Step6 frame={frame} opacity={step6Opacity} />
      )}
      {frame >= STEP7_START - FADE && (
        <Step7 frame={frame} opacity={step7Opacity} />
      )}
    </AbsoluteFill>
  );
};
