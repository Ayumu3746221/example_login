import React from "react";
import { auth } from "@/auth";

async function ContentPage() {
  const session = await auth();

  return <pre>{JSON.stringify(session, null, 2)}</pre>;
}

export default ContentPage;
