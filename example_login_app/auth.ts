import NextAuth, { Account, Session } from "next-auth";
import { JWT } from "next-auth/jwt";
import Google from "next-auth/providers/google";

export const { handlers, signIn, signOut, auth } = NextAuth({
  providers: [
    Google({
      clientId: process.env.GOOGLE_ID,
      clientSecret: process.env.GOOGLE_SECRET,
      authorization: {
        params: {
          scope:
            "openid email profile https://www.googleapis.com/auth/drive.readonly",
          access_type: "offline",
        },
      },
    }),
  ],
  pages: {
    signIn: "/login",
  },
  session: {
    strategy: "jwt",
  },
  callbacks: {
    async signIn({ profile }) {
      if (!profile) {
        return "/login?error=PrifileNotFound";
      }

      if (profile?.email !== process.env.NEXT_ALLOW_EMAIL) {
        return "/login?error=NotAuthorizedAccess";
      }

      return true;
    },
    async jwt({ token, account }: { token: JWT; account: Account | null }) {
      //Firest-time login, save the `access_token` , its expiry and the `refresh_token`
      if (account) {
        return {
          ...token,
          access_token: account.access_token,
          expires_at:
            account.expires_at || Math.floor(Date.now() / 1000) + 3600,
          refresh_token: account.refresh_token,
          id_token: account.id_token,
        };
      } else if (Date.now() < token.expires_at * 1000) {
        return token;
      } else {
        if (!token.refresh_token) throw new TypeError("Missing refresh_token");

        try {
          const response = await fetch("https://oauth2.googleapis.com/token", {
            method: "POST",
            body: new URLSearchParams({
              client_id: process.env.AUTH_GOOGLE_ID!,
              client_secret: process.env.AUTH_GOOGLE_SECRET!,
              grant_type: "refresh_token",
              refresh_token: token.refresh_token!,
            }),
          });

          const tokensOrError = await response.json();

          if (!response.ok) throw tokensOrError;

          const newTokens = tokensOrError as {
            access_token: string;
            expires_in: number;
            refresh_token?: string;
            id_token: string;
          };

          token.access_token = newTokens.access_token;
          token.expires_at = Math.floor(
            Date.now() / 1000 + newTokens.expires_in
          );

          if (newTokens.refresh_token)
            token.refresh_token = newTokens.refresh_token;

          token.id_token = newTokens.id_token;
          return token;
        } catch (error) {
          console.error("Error refreshing access_token", error);
          token.error = "RefreshTokenError";

          return token;
        }
      }
    },
    async session({ session, token }: { session: Session; token: JWT }) {
      const userEmail = token?.email;

      if (userEmail === process.env.NEXT_ALLOW_EMAIL) {
        session.access_token = token.access_token;
        session.expires_at = token.expires_at;
        session.refresh_token = token.refresh_token;
        session.id_token = token.id_token;
      }

      if (token.error) {
        session.error = token.error;
      }

      return session;
    },
    async authorized({ request, auth }) {
      const url = request.nextUrl;
      const allowedEmail = process.env.NEXT_ALLOW_EMAIL;

      try {
        const { pathname } = url;

        if (pathname.startsWith("/dashboard")) {
          return Boolean(auth);
        }

        if (request.method === "POST") {
          const userEmail = auth?.user?.email;

          if (userEmail !== allowedEmail) {
            return false;
          }

          return Boolean(auth);
        }
        return true;
      } catch (err) {
        console.log("logging error", err);
      }
    },
  },
});
