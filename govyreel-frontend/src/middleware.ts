import { jwtVerify } from "jose";
import { NextRequest, NextResponse } from "next/server";
import { getJwtSecret } from "./lib/auth";
import { JWTPayload } from "./types/jwt";

const PROTECTED_PATHS = {
  paths: [
    {
      path: "/workspace",
      roles: ["ALL"],
    },
    {
      path: "/other",
      roles: ["ALL"],
    }
  ],
};

export async function middleware(request: NextRequest) {
  const accessTokenCookie = request.cookies.get("accessToken");
  const refreshTokenCookie = request.cookies.get("refreshToken");
  const accessToken = accessTokenCookie?.value;

  if (!accessToken || accessToken === "") {
    if (!refreshTokenCookie || refreshTokenCookie.value === "") {
      return NextResponse.redirect(new URL("/login", request.url));
    }
    return NextResponse.redirect(new URL("/refresh-token?redirectsTo=" + request.nextUrl.pathname, request.url));
  }

  try {
    const jwtSecret = await getJwtSecret();
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
        return NextResponse.redirect(new URL("/refresh-token?redirectsTo=" + request.url, request.url));
      }
    }

    console.error("Error in middleware:", error);
    return NextResponse.redirect(new URL("/login", request.url));
  }

  return;
}



export const config = {
  matcher: [
    "/workspace", "/workspace/:path*",
    "/other", "/other/:path*"
  ],
};
