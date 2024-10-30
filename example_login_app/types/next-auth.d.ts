import { Session } from "inspector";
import { Session as DefaultSession } from "next-auth";
import { JWT, DefaultJWT } from "next-auth/jwt";

declare module "next-auth/jwt" {
  interface JWT extends DefaultJWT {
    access_token?: string;
    expires_at: number;
    refresh_token?: string;
    id_token?: string;
    error?: "RefreshTokenError";
  }
}

declare module "next-auth" {
  interface Session extends DefaultSession {
    access_token?: string;
    expires_at: number;
    refresh_token?: string;
    id_token?: string;
    error?: "RefreshTokenError";
  }
}
