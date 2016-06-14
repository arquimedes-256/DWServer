var gf = PolymerExpressions.prototype;

gf.not = function(value)
{
  return !value;
};
gf.is_object = function(value)
{
  return gf.pwdk(value) == "OBJECT";
};
gf.is_class = function(value)
{
  return gf.pwdk(value) == "CLASS";
};
gf.iowd = function(value)
{
  var b = value && value.split('/').length > 4;
  return b;
};
gf.string_base = function(value)
{
  return STRING_MAP[value];
};
gf.pwdt = function(value)
{
  var b = value && value.match(/\//g).length;
  return STRING_MAP[gf.pwdk(b)];
};
gf.pwdk = function(value,offset)
{
  if(!offset)
    offset = 0;
    
  var b = value && value.match(/\//g).length + offset;
  var r;
  switch(b)
  {
    case 1:
      return "APPLICATION";
      break;
    case 2:
      return "MODULE";
      break;
    case 3:
      return "CLASS";
      break;
    case 4:
      return "OBJECT";
      break;
  }
};