{
  description = "arithmetic";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem
      (system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
          deps = with pkgs; [
            mill
            circt
            verilator
          ];
        in {
          legacyPackages = pkgs;
          devShells.default = pkgs.mkShell { buildInputs = deps; };
        }
      );
}
