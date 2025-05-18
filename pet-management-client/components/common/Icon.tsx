import { GiCat } from "react-icons/gi";

interface IconProps {
  className?: string;
  showText?: boolean;
}

const Icon = ({ className = "", showText = true }: IconProps) => {
  return (
    <div className={`flex gap-2 items-center ${className}`}>
      <GiCat size={32} />
      {showText && (
        <h1 className="cursor-pointer text-3xl font-semibold">
          Pet<span className="text-brand">Care88</span>
        </h1>
      )}
    </div>
  );
};

export default Icon;
