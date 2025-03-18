import React, { createContext, useState, useContext } from 'react';

export const SoundContext = createContext();

export const useSound = () => useContext(SoundContext); // Новый хук

export const SoundProvider = ({ children }) => {
  const [isSoundEnabled, setIsSoundEnabled] = useState(true);
  
  return (
    <SoundContext.Provider value={{ isSoundEnabled, setIsSoundEnabled }}>
      {children}
    </SoundContext.Provider>
  );
};