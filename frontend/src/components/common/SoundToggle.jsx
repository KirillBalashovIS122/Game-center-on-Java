import React from 'react';
import { SoundContext } from '../../contexts/SoundContext';

const SoundToggle = () => {
  const { isSoundEnabled, setIsSoundEnabled } = React.useContext(SoundContext);

  return (
    <button onClick={() => setIsSoundEnabled(!isSoundEnabled)}>
      {isSoundEnabled ? 'Выключить звук' : 'Включить звук'}
    </button>
  );
};

export default SoundToggle;