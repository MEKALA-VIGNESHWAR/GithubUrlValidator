import React, { useEffect } from 'react';

export default function Toast({ toast, onClose }) {
  useEffect(() => {
    if (!toast) return;
    const timer = setTimeout(() => {
      onClose();
    }, 4000);
    return () => clearTimeout(timer);
  }, [toast, onClose]);

  if (!toast) return null;

  const isSuccess = toast.type === 'success';

  return (
    <div
      className="animate-fade-in"
      style={{
        position: 'fixed',
        bottom: '24px',
        right: '24px',
        zIndex: 1000,
        background: isSuccess ? 'rgba(6, 78, 59, 0.95)' : 'rgba(127, 29, 29, 0.95)',
        border: `1px solid ${isSuccess ? '#10b981' : '#ef4444'}`,
        color: isSuccess ? '#a7f3d0' : '#fecaca',
        padding: '14px 20px',
        borderRadius: '10px',
        boxShadow: '0 10px 25px rgba(0, 0, 0, 0.5)',
        display: 'flex',
        alignItems: 'center',
        gap: '12px',
        fontSize: '0.9rem',
        fontWeight: '600'
      }}
    >
      <span>{isSuccess ? '✅' : '⚠️'}</span>
      <span>{toast.message}</span>
      <button
        onClick={onClose}
        style={{
          background: 'none',
          border: 'none',
          color: 'inherit',
          cursor: 'pointer',
          fontSize: '1.1rem',
          marginLeft: '8px'
        }}
      >
        ×
      </button>
    </div>
  );
}
