import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import "./styles/global.css";
import App from "./App";
import { AuthProvider } from "./auth/AuthContext";
import { BrowserRouter } from 'react-router-dom';

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <AuthProvider>
      <BrowserRouter basename="/captain-mercer/">
        <App />
      </BrowserRouter>
    </AuthProvider>
  </React.StrictMode>
);

