import React from "react";
import SignIn from "../../components/authencation_components/SignIn";

const LoginPage: React.FC = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="max-w-md w-full space-y-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            ログインページ
          </h2>
        </div>
        <div>
          <SignIn></SignIn>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
