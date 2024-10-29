import { auth } from "@/auth";
import { data } from "autoprefixer";
import { redirect } from "next/navigation";
import React from "react";

function FeachButton() {
  return (
    <form
      action={async () => {
        "use server";

        const session = await auth();
        console.log("access_token", session?.access_token);

        try {
          await fetch("http://localhost:8080/api/test", {
            headers: {
              Authorization: `Bearer ${session?.access_token}`,
            },
            method: "GET",
          })
            .then((response) => {
              if (response.ok) {
                return response.json();
              }
              throw new Error("Network response was not OK");
            })
            .then((data) => {
              console.log("Response Data:", data);
            });
        } catch (error) {
          console.error("There was a problem with the fetch operation:", error);
        }
      }}
    >
      <button
        type="submit"
        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
      >
        Click!!
      </button>
    </form>
  );
}

export default FeachButton;
