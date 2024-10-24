const fs = require('fs');
const path = require('path');

const libraryBasePath = 'libs/';
const tagMappings = {};

function scanDirectories(basePath) {
  const domains = fs.readdirSync(basePath);
  domains.forEach(domain => {
    const domainPath = path.join(basePath, domain);
    if (fs.lstatSync(domainPath).isDirectory()) {
      // Define domain-level tags
      tagMappings[domainPath] = [`domain:${domain}`];
      const types = fs.readdirSync(domainPath);
      types.forEach(type => {
        const typePath = path.join(domainPath, type);
        if (fs.lstatSync(typePath).isDirectory()) {
          // Define type-level tags
          tagMappings[typePath] = [`type:${type}`, `domain:${domain}`];
        }
      });
    }
  });
}

scanDirectories(libraryBasePath);

// Write this to a JSON file or directly to eslint.config.ts
fs.writeFileSync('eslint-tag-mappings.json', JSON.stringify(tagMappings, null, 2));
