import Image from "next/image";

const Loading = () => {
  return (
    <div className="fixed inset-0 bg-slate-900/20 p-8 z-[9999] backdrop-blur-sm flex flex-col items-center justify-center space-y-3 select-none">
      <Image
        src="/assets/images/Seia_ball.png"
        alt="Loading"
        width={500}
        height={400}
        className="size-72 animate-pulse duration-75"
      />
      <div className="text-white font-bold text-3xl animate-pulse duration-100">
        Now Loading ...
      </div>
    </div>
  );
};

export default Loading;
