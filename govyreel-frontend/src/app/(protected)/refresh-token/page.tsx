import { RefreshToken } from "@/components/auth/RefreshToken";

export default async function RefreshTokenPage({
  searchParams,
}: {
  searchParams: { redirectsTo: string };
}) {
  // The await here is required as per next js docs https://nextjs.org/docs/messages/sync-dynamic-apis
  const { redirectsTo } = await searchParams;
  return (
    <RefreshToken redirectsTo={redirectsTo} />
  )
}
