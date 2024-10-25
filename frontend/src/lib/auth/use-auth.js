import useSWR from "swr";
import { useEffect } from "react";
import { useRouter } from "next/navigation";
import httpClient from "../httpClient";

export const useAuthGuard = ({
  middleware,
  redirectIfAuthenticated,
}) => {
  const router = useRouter();

  const {
    data: user,
    error,
    mutate,
  } = useSWR("/api/users/me", async () =>
     await httpClient.get("/api/users/me").then((res) => res.data)
  );


  const logout = async () => {
    try {
      await httpClient.post("/api/users/logout");
      mutate("/api/users/me", null, false); 
      await mutate("/api/users/me");
      router.push("/"); 
    } catch (err) {
      console.error("Logout error:", err);
    }
  };
  
  

  useEffect(() => {
    if (middleware === "guest" && redirectIfAuthenticated && user) {
      console.log("auth gurd  " + redirectIfAuthenticated)
      router.push(redirectIfAuthenticated);
    }

    if (middleware === "auth" && error) {
      logout();
    }
  }, [user, error]);

  return {
    user,
    logout,
    mutate,
  };
};
