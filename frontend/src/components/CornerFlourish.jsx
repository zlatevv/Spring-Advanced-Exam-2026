function Ornament() {
  return (
    <svg viewBox="0 0 30 30" xmlns="http://www.w3.org/2000/svg">
      <path
        d="M2 28 C2 14, 14 2, 28 2"
        fill="none"
        stroke="#a9873f"
        strokeWidth="1.1"
      />
      <circle cx="28" cy="2" r="2" fill="#a9873f" />
      <circle cx="2" cy="28" r="2" fill="#a9873f" />
      <path d="M6 24 C8 16, 16 8, 24 6" fill="none" stroke="#a9873f" strokeWidth="0.6" />
    </svg>
  );
}

export default function CornerFlourish({ corner }) {
  return (
    <span className={`corner-flourish ${corner}`}>
      <Ornament />
    </span>
  );
}
