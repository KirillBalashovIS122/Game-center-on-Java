import React, { useContext } from 'react';
import { SoundContext } from '../../contexts/SoundContext';

const SoundToggle = () => {
    const { isSoundOn, setIsSoundOn } = useContext(SoundContext);
    
    return (
        <button 
            onClick={() => setIsSoundOn(!isSoundOn)}
            className="sound-toggle"
        >
            {isSoundOn ? "🔊" : "🔇"}
        </button>
    );
};

export default SoundToggle;