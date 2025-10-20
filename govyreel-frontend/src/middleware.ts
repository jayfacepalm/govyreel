import { jwtVerify } from "jose";
import { NextRequest, NextResponse } from "next/server";
import { refreshTokenServerAction } from "./app/login/action";

type JWTPayload = {
  sub: string;
  iat: number;
  exp: number;
  displayName: string;
  roles: string[];
};

const PROTECTED_PATHS = {
  paths: [
    {
      path: "/dashboard",
      roles: ["ALL"],
    },
  ],
};

export async function middleware(request: NextRequest) {
  const accessTokenCookie = request.cookies.get("accessToken");
  const accessToken = accessTokenCookie?.value;

  if (!accessToken || accessToken === "") {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  try {
    const jwtSecret = getJwtSecret();
    const { payload } = await jwtVerify(accessToken, jwtSecret);
    const { roles } = payload as JWTPayload;
    const { sub } = payload as JWTPayload;

    for (const protectedPath of PROTECTED_PATHS.paths) {
      if (request.nextUrl.pathname.startsWith(protectedPath.path)) {
        if (
          !protectedPath.roles.includes("ALL") &&
          !protectedPath.roles.some((role) => roles.includes(role))
        ) {
          console.error(
            "User does not have the required role to access this path.",
            request.nextUrl.pathname,
            sub,
            roles
          );
          return NextResponse.redirect(new URL("/login", request.url));
        }
      }
    }
  } catch (error) {
    if (error instanceof Error) {
      if ("code" in error && error.code === "ERR_JWT_EXPIRED") {
        const refreshTokenCookie = request.cookies.get("refreshToken");
        const refreshToken = refreshTokenCookie?.value;

        if (!refreshToken || refreshToken === "") {
          return NextResponse.redirect(new URL("/login", request.url));
        }

        refreshTokenServerAction();
        // After refreshing token, redirect to the same request URL to retry with new token
        return NextResponse.redirect(new URL(request.url, request.url));
      }
    }

    console.error("Error in middleware:", error);
    return NextResponse.redirect(new URL("/login", request.url));
  }

  return;
}

export const config = {
  matcher: ["/dashboard", "/dashboard/:path*"],
};

function getJwtSecret(): Uint8Array {
  const secret = process.env.JWT_SECRET_KEY;
  if (!secret) {
    throw new Error("JWT_SECRET_KEY environment variable is not set");
  }
  return new TextEncoder().encode(secret);
}
