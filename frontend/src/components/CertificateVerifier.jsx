import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function CertificateVerifier({ token: propToken }) {
  const [tokenInput, setTokenInput] = useState(propToken || 'HF-CERT-8F3A2B1C');
  const [certData, setCertData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const verifyCertificate = async (tokenToVerify) => {
    if (!tokenToVerify) return;
    setLoading(true);
    setError(null);
    try {
      const res = await axios.get(`/verify/certificate/${tokenToVerify.trim().toUpperCase()}`);
      setCertData(res.data);
    } catch (err) {
      console.error("Certificate verification failed", err);
      setError("Invalid or unverified certificate verification hash token.");
      setCertData(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (propToken) {
      verifyCertificate(propToken);
    } else {
      verifyCertificate('HF-CERT-8F3A2B1C');
    }
  }, [propToken]);

  return (
    <div className="bg-slate-900 text-slate-100 min-h-screen p-6 font-sans flex items-center justify-center">
      <div className="max-w-3xl w-full space-y-8">

        {/* Header Search Card */}
        <div className="bg-slate-800/80 border border-slate-700/80 rounded-2xl p-8 shadow-2xl backdrop-blur-xl space-y-6">
          <div className="text-center space-y-2">
            <div className="inline-flex items-center space-x-2 px-3 py-1 bg-amber-500/10 border border-amber-500/30 rounded-full text-amber-400 text-xs font-semibold uppercase tracking-wider">
              <span>🏅 Cryptographic Open Badges 2.0 Verifier</span>
            </div>
            <h1 className="text-3xl font-extrabold text-white">Public Certificate Verification</h1>
            <p className="text-slate-400 text-sm max-w-lg mx-auto">
              Enter any HackForge issued verification token to independently validate authenticity, Open Badges W3C assertion, and download high-resolution PDF credentials.
            </p>
          </div>

          <div className="flex gap-3 max-w-xl mx-auto">
            <input
              type="text"
              value={tokenInput}
              onChange={(e) => setTokenInput(e.target.value)}
              placeholder="e.g. HF-CERT-8F3A2B1C"
              className="flex-1 px-4 py-3 bg-slate-900 border border-slate-700 rounded-xl text-white font-mono text-sm focus:outline-none focus:border-amber-500"
            />
            <button
              onClick={() => verifyCertificate(tokenInput)}
              className="px-6 py-3 bg-amber-500 hover:bg-amber-400 text-slate-950 font-bold text-sm rounded-xl transition shadow-lg"
            >
              Verify Certificate
            </button>
          </div>
        </div>

        {/* Verification Status Output */}
        {loading && (
          <div className="text-center p-8 bg-slate-800/40 rounded-2xl border border-slate-700/50">
            <div className="animate-spin text-3xl mb-2">⚡</div>
            <p className="text-slate-400 text-sm">Validating cryptographic digital signature on blockchain & S3 ledger...</p>
          </div>
        )}

        {error && (
          <div className="p-6 bg-rose-500/10 border border-rose-500/30 rounded-2xl text-rose-400 text-center font-semibold">
            ❌ {error}
          </div>
        )}

        {certData && certData.valid && (
          <div className="bg-gradient-to-br from-slate-800 via-slate-850 to-slate-900 border border-emerald-500/40 rounded-2xl p-8 shadow-2xl space-y-6 relative overflow-hidden">
            {/* Watermark Ribbon */}
            <div className="absolute -right-12 -top-12 bg-emerald-500 text-slate-950 text-xs font-black uppercase tracking-widest py-10 px-16 rotate-45 shadow-lg">
              VERIFIED AUTHENTIC
            </div>

            <div className="flex items-center space-x-3 text-emerald-400 font-semibold text-sm">
              <span className="text-xl">✅</span>
              <span>Valid W3C Open Badges Credential Assertion</span>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-2">
              <div className="md:col-span-2 space-y-4">
                <div>
                  <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Recipient Name</span>
                  <h2 className="text-2xl font-bold text-white mt-1">{certData.recipientName}</h2>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Achievement Type</span>
                    <p className="text-sm font-semibold text-indigo-400 mt-1">{certData.certificateType} Certificate</p>
                  </div>
                  <div>
                    <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Issue Timestamp</span>
                    <p className="text-xs font-mono text-slate-300 mt-1">{certData.issuedAt}</p>
                  </div>
                </div>

                <div>
                  <span className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Unique Hash Token</span>
                  <p className="text-xs font-mono text-emerald-400 bg-slate-950 px-3 py-2 rounded-lg border border-slate-800 mt-1">
                    {certData.verificationToken}
                  </p>
                </div>
              </div>

              {/* QR Code & PDF Actions */}
              <div className="flex flex-col items-center justify-center p-4 bg-slate-900/80 rounded-xl border border-slate-700/80 space-y-3">
                <img
                  src={certData.qrCodeUrl}
                  alt="Certificate Verification QR Code"
                  className="w-36 h-36 rounded-lg border border-slate-700 p-1 bg-white"
                />
                <a
                  href={certData.pdfUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="w-full text-center py-2 bg-emerald-600 hover:bg-emerald-500 text-white text-xs font-semibold rounded-lg transition shadow"
                >
                  📄 Download Official PDF
                </a>
              </div>
            </div>

            {/* Open Badges Raw JSON Spec preview */}
            <div className="pt-4 border-t border-slate-700/60">
              <details className="cursor-pointer">
                <summary className="text-xs font-mono text-slate-400 hover:text-slate-200">
                  🔍 View Open Badges 2.0 JSON Assertion Metadata
                </summary>
                <pre className="mt-3 p-4 bg-slate-950 text-emerald-400 font-mono text-[11px] rounded-lg border border-slate-800 overflow-x-auto">
                  {JSON.stringify(certData.openBadgeAssertion, null, 2)}
                </pre>
              </details>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
