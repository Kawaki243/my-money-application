import { createContext } from "react";
import React, { useState } from "react";

// 1. Create the context
export const AppContext = createContext();

// 2. Create a provider component
// access the context via useContext(AppContext)
export const AppContextProvider = ({ children }) => {
	// This is the data (state, functions, etc.) you want to share

	const [user, setUser] = useState(null);

      const clearUser = () => {
            setUser(null);
      }
	
      const contextValue = {
            // e.g. user, theme, functions, etc.
		user,
            setUser,
            clearUser
      };

      return (
            <AppContext.Provider value={contextValue}>
                  {children}
            </AppContext.Provider>
      );
};

