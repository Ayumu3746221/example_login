import React from "react";
import { auth } from "@/auth";
import { cookies } from "next/headers";

async function ContentPage() {
  const session = await auth();
  const cookieStore = cookies();
  const cookie = cookieStore.getAll();

  return (
    <>
      <pre>{JSON.stringify(cookie, null, 2)}</pre>
      <pre>{JSON.stringify(session, null, 2)}</pre>
    </>
  );
}

export default ContentPage;
