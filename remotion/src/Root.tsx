import React from "react";
import { Composition } from "remotion";
import { FormidableIntro } from "./FormidableIntro";

export const RemotionRoot: React.FC = () => {
  return (
    <Composition
      id="FormidableIntro"
      component={FormidableIntro}
      durationInFrames={890}
      fps={30}
      width={1280}
      height={720}
    />
  );
};