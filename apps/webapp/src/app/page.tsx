import React from "react";
import {SectionCards} from "@/components/section-cards";

const RootPage = ({children,}: Readonly<{ children: React.ReactNode; }>) => {
  return (
    <SectionCards/>
  );
}

export default RootPage;