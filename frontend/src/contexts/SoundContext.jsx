import React, { createContext, useContext, useState } from 'react';

export const SoundContext = createContext();

export const SoundProvider = ({ children }) => {
    const [isSoundOn, setIsSoundOn] = useState(true);

    const playSound = (soundFile) => {
        if (isSoundOn) {
            new Audio(`/sounds/${soundFile}`).play().catch(() => {});
        }
    };

    return (
        <SoundContext.Provider value={{ isSoundOn, setIsSoundOn, playSound }}>
            {children}
        </SoundContext.Provider>
    );
};

export const useSound = () => {
    const context = useContext(SoundContext);
    if (!context) {
        throw new Error('useSound must be used within a SoundProvider');
    }
    return context;
};