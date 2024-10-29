import React from "react";
import SignOut from "@/components/authencation_components/SignOut";
import FeachButton from "@/components/button/feachButton";

const Dashboard: React.FC = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="max-w-md w-full space-y-8 p-10 bg-white shadow rounded-xl">
        <div className="text-center">
          <h1 className="text-3xl font-extrabold text-gray-900">Dashboard</h1>
          <p className="mt-2 text-sm text-gray-600">Success Logging in!!</p>
        </div>
        <div className="mt-8 space-y-4">
          <FeachButton></FeachButton>
          <SignOut></SignOut>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
